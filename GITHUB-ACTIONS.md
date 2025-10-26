# GitHub Actions CI/CD Setup Guide

이 문서는 Team-IT 프로젝트의 GitHub Actions를 사용한 자동 배포 설정 방법을 설명합니다.

## 목차
1. [Workflows 개요](#workflows-개요)
2. [GitHub Secrets 설정](#github-secrets-설정)
3. [AWS 리소스 설정](#aws-리소스-설정)
4. [워크플로우 사용법](#워크플로우-사용법)
5. [트러블슈팅](#트러블슈팅)

## Workflows 개요

### 1. CI for Development (`ci-dev.yml`)
- **트리거**: `develop`, `feature/**` 브랜치 푸시 및 PR
- **동작**:
  - 테스트 실행
  - JAR 빌드
  - Docker 이미지 빌드 및 ECR 푸시 (develop 브랜치만)

### 2. Deploy to AWS Production (`deploy-prod.yml`)
- **트리거**: `main` 브랜치 푸시 또는 수동 실행
- **동작**:
  - 테스트 실행
  - Docker 이미지 빌드
  - ECR에 푸시
  - ECS 서비스 업데이트 (Fargate)

### 3. Deploy to EC2 (`deploy-ec2.yml`)
- **트리거**: `main` 브랜치 푸시 또는 수동 실행
- **동작**:
  - 테스트 실행
  - Docker 이미지 빌드
  - ECR에 푸시
  - EC2 인스턴스에 SSH 접속하여 배포

## GitHub Secrets 설정

GitHub Repository → Settings → Secrets and variables → Actions에서 다음 Secrets를 추가하세요.

### 필수 Secrets

#### AWS 관련
```
AWS_ACCESS_KEY_ID=AKIA...
AWS_SECRET_ACCESS_KEY=your_secret_access_key
AWS_ACCOUNT_ID=123456789012
```

#### EC2 배포용 (deploy-ec2.yml 사용 시)
```
EC2_HOST=ec2-xx-xxx-xxx-xxx.ap-northeast-2.compute.amazonaws.com
EC2_USERNAME=ec2-user
EC2_SSH_KEY=-----BEGIN RSA PRIVATE KEY-----
...
-----END RSA PRIVATE KEY-----
```

### AWS IAM 사용자 권한

GitHub Actions에서 사용할 IAM 사용자에 다음 권한이 필요합니다:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "ecr:PutImage",
        "ecr:InitiateLayerUpload",
        "ecr:UploadLayerPart",
        "ecr:CompleteLayerUpload"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "ecs:UpdateService",
        "ecs:DescribeServices",
        "ecs:DescribeTaskDefinition",
        "ecs:RegisterTaskDefinition"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "iam:PassRole"
      ],
      "Resource": [
        "arn:aws:iam::*:role/ecsTaskExecutionRole",
        "arn:aws:iam::*:role/ecsTaskRole"
      ]
    }
  ]
}
```

## AWS 리소스 설정

### 1. ECR Repository 생성

```bash
# Production용
aws ecr create-repository \
    --repository-name team-it-app \
    --region ap-northeast-2 \
    --image-scanning-configuration scanOnPush=true

# Development용
aws ecr create-repository \
    --repository-name team-it-app-dev \
    --region ap-northeast-2 \
    --image-scanning-configuration scanOnPush=true
```

### 2. AWS Secrets Manager에 비밀 정보 저장

```bash
# Database URL
aws secretsmanager create-secret \
    --name team-it/database-url \
    --description "Team-IT Database URL" \
    --secret-string "jdbc:mysql://your-rds-endpoint:3306/teamit?useSSL=true&serverTimezone=UTC"

# Database credentials
aws secretsmanager create-secret \
    --name team-it/database \
    --description "Team-IT Database Credentials" \
    --secret-string '{
        "username": "admin",
        "password": "your-secure-password"
    }'

# JWT Secret
aws secretsmanager create-secret \
    --name team-it/jwt-secret \
    --description "Team-IT JWT Secret Key" \
    --secret-string "your-production-jwt-secret-key"
```

### 3. ECS Task Definition 업데이트

`.aws/task-definition.json` 파일에서 다음 값을 업데이트:

- `YOUR_ACCOUNT_ID`: AWS 계정 ID
- `executionRoleArn`: ECS Task Execution Role ARN
- `taskRoleArn`: ECS Task Role ARN
- Secrets Manager ARN들

### 4. ECS 클러스터 및 서비스 생성

```bash
# 클러스터 생성
aws ecs create-cluster \
    --cluster-name team-it-cluster \
    --region ap-northeast-2

# VPC 및 서브넷 정보 확인
aws ec2 describe-subnets --region ap-northeast-2

# 보안 그룹 정보 확인
aws ec2 describe-security-groups --region ap-northeast-2

# 서비스 생성
aws ecs create-service \
    --cluster team-it-cluster \
    --service-name team-it-service \
    --task-definition team-it-task:1 \
    --desired-count 2 \
    --launch-type FARGATE \
    --network-configuration "awsvpcConfiguration={
        subnets=[subnet-xxx,subnet-yyy],
        securityGroups=[sg-xxx],
        assignPublicIp=ENABLED
    }" \
    --load-balancers "targetGroupArn=arn:aws:elasticloadbalancing:ap-northeast-2:xxx:targetgroup/team-it-tg/xxx,containerName=team-it-app,containerPort=8080"
```

### 5. CloudWatch Logs 그룹 생성

```bash
aws logs create-log-group \
    --log-group-name /ecs/team-it \
    --region ap-northeast-2
```

## 워크플로우 사용법

### 자동 배포

1. **개발 환경**
   ```bash
   git checkout develop
   git add .
   git commit -m "feat: new feature"
   git push origin develop
   ```
   → `ci-dev.yml` 자동 실행

2. **프로덕션 환경**
   ```bash
   git checkout main
   git merge develop
   git push origin main
   ```
   → `deploy-prod.yml` 또는 `deploy-ec2.yml` 자동 실행

### 수동 배포

GitHub → Actions → 워크플로우 선택 → "Run workflow" 버튼 클릭

## ECS 배포 vs EC2 배포

### ECS/Fargate 배포 (`deploy-prod.yml`)
**장점:**
- 자동 스케일링
- 로드 밸런싱 통합
- 서버리스 (인프라 관리 불필요)
- 무중단 배포

**단점:**
- 비용이 EC2보다 높을 수 있음
- 초기 설정이 복잡

**적합한 경우:**
- 트래픽 변동이 큰 경우
- 고가용성이 중요한 경우
- 운영 부담을 최소화하려는 경우

### EC2 배포 (`deploy-ec2.yml`)
**장점:**
- 비용이 저렴
- 설정이 간단
- 서버에 직접 접근 가능

**단점:**
- 수동 스케일링
- 인프라 관리 필요
- 배포 중 다운타임 발생 가능

**적합한 경우:**
- 예산이 제한적인 경우
- 트래픽이 안정적인 경우
- 서버에 직접 접근이 필요한 경우

## 브랜치 전략

```
main (프로덕션)
  ↑
develop (개발)
  ↑
feature/* (기능 개발)
```

1. `feature/*` 브랜치에서 개발
2. `develop` 브랜치로 PR 및 병합
3. `develop`에서 테스트 후 `main`으로 병합

## 워크플로우 상태 모니터링

### GitHub Actions UI
- Repository → Actions 탭에서 워크플로우 실행 상태 확인
- 각 단계별 로그 확인 가능

### AWS CloudWatch
```bash
# ECS 서비스 로그 확인
aws logs tail /ecs/team-it --follow
```

### Slack 알림 추가 (선택사항)

워크플로우에 Slack 알림 추가:

```yaml
- name: Notify Slack
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'Deployment to Production'
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
  if: always()
```

## 트러블슈팅

### 1. ECR 푸시 실패

**문제**: `denied: Your authorization token has expired`

**해결**:
```bash
# IAM 사용자 권한 확인
aws iam get-user

# ECR 권한 확인
aws ecr describe-repositories
```

### 2. ECS 배포 실패

**문제**: Task가 시작하지 않음

**해결**:
```bash
# Task 로그 확인
aws ecs describe-tasks \
    --cluster team-it-cluster \
    --tasks TASK_ARN

# CloudWatch 로그 확인
aws logs tail /ecs/team-it --follow
```

### 3. Health Check 실패

**문제**: 컨테이너가 Unhealthy 상태

**해결**:
- Spring Boot Actuator 의존성 확인
- `application.properties`에 actuator 엔드포인트 활성화:
  ```properties
  management.endpoints.web.exposure.include=health
  management.endpoint.health.show-details=always
  ```

### 4. Secrets 접근 실패

**문제**: `AccessDeniedException` for Secrets Manager

**해결**:
- ECS Task Execution Role에 Secrets Manager 권한 추가:
  ```json
  {
    "Effect": "Allow",
    "Action": [
      "secretsmanager:GetSecretValue"
    ],
    "Resource": "arn:aws:secretsmanager:*:*:secret:team-it/*"
  }
  ```

## 배포 롤백

### ECS 배포 롤백

```bash
# 이전 Task Definition 버전으로 롤백
aws ecs update-service \
    --cluster team-it-cluster \
    --service team-it-service \
    --task-definition team-it-task:PREVIOUS_VERSION
```

### EC2 배포 롤백

```bash
# SSH로 EC2 접속
ssh -i your-key.pem ec2-user@your-ec2-host

# 이전 이미지로 롤백
docker pull YOUR_ECR_REPOSITORY:PREVIOUS_TAG
docker tag YOUR_ECR_REPOSITORY:PREVIOUS_TAG team-it-app:latest
docker-compose -f docker-compose.prod.yml up -d
```

## 성능 최적화

### Docker 이미지 최적화
- 멀티스테이지 빌드 사용 (이미 적용됨)
- `.dockerignore` 파일 활용
- 레이어 캐싱 최적화

### GitHub Actions 캐싱
- Gradle 캐시 활용 (이미 적용됨)
- Docker 레이어 캐싱

## 보안 Best Practices

- [ ] AWS Secrets Manager로 민감 정보 관리
- [ ] IAM 역할은 최소 권한 원칙 적용
- [ ] ECR 이미지 스캔 활성화
- [ ] VPC 내부에서만 데이터베이스 접근
- [ ] HTTPS 사용 (ALB에서 SSL/TLS 종료)
- [ ] 정기적인 보안 업데이트
- [ ] GitHub Secrets 암호화 사용

## 참고 자료

- [GitHub Actions 문서](https://docs.github.com/en/actions)
- [AWS ECS 문서](https://docs.aws.amazon.com/ecs/)
- [AWS ECR 문서](https://docs.aws.amazon.com/ecr/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
