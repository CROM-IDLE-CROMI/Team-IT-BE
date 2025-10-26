# AWS Deployment Guide

이 문서는 Team-IT 애플리케이션을 AWS에 배포하는 방법을 설명합니다.

## 목차
1. [사전 요구사항](#사전-요구사항)
2. [AWS RDS MySQL 설정](#aws-rds-mysql-설정)
3. [Docker 이미지 빌드 및 ECR 푸시](#docker-이미지-빌드-및-ecr-푸시)
4. [EC2에서 Docker 컨테이너 실행](#ec2에서-docker-컨테이너-실행)
5. [ECS/Fargate 배포 (선택사항)](#ecsfargate-배포)
6. [환경 변수 설정](#환경-변수-설정)

## 사전 요구사항

- AWS 계정
- AWS CLI 설치 및 구성
- Docker 설치
- 도메인 (선택사항)

## AWS RDS MySQL 설정

### 1. RDS 인스턴스 생성

```bash
# AWS Console 또는 CLI로 RDS MySQL 생성
aws rds create-db-instance \
    --db-instance-identifier teamit-db \
    --db-instance-class db.t3.micro \
    --engine mysql \
    --engine-version 8.0 \
    --master-username admin \
    --master-user-password YOUR_SECURE_PASSWORD \
    --allocated-storage 20 \
    --vpc-security-group-ids sg-xxxxxxxxx \
    --db-subnet-group-name your-subnet-group \
    --backup-retention-period 7 \
    --publicly-accessible false
```

### 2. 보안 그룹 설정

RDS 보안 그룹에서 EC2 인스턴스의 보안 그룹으로부터 3306 포트 인바운드 허용

### 3. 데이터베이스 생성

```sql
CREATE DATABASE teamit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## Docker 이미지 빌드 및 ECR 푸시

### 1. ECR 레포지토리 생성

```bash
aws ecr create-repository --repository-name team-it-app --region ap-northeast-2
```

### 2. ECR 로그인

```bash
aws ecr get-login-password --region ap-northeast-2 | \
docker login --username AWS --password-stdin YOUR_AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-2.amazonaws.com
```

### 3. Docker 이미지 빌드 및 푸시

```bash
# 이미지 빌드
docker build -t team-it-app:latest .

# 이미지 태그
docker tag team-it-app:latest \
    YOUR_AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-2.amazonaws.com/team-it-app:latest

# ECR에 푸시
docker push YOUR_AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-2.amazonaws.com/team-it-app:latest
```

## EC2에서 Docker 컨테이너 실행

### 1. EC2 인스턴스 준비

```bash
# Docker 설치
sudo yum update -y
sudo yum install -y docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# Docker Compose 설치
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
    -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. 환경 변수 설정

```bash
# .env.prod 파일 생성
cat > .env.prod << EOF
DATABASE_URL=jdbc:mysql://your-rds-endpoint.region.rds.amazonaws.com:3306/teamit?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=admin
DB_PASSWORD=your_secure_password
JWT_SECRET=your_production_jwt_secret_minimum_256_bits
JWT_EXPIRATION_ACCESS=3600000
JWT_EXPIRATION_REFRESH=604800000
DDL_AUTO=validate
PORT=8080
EOF
```

### 3. ECR에서 이미지 Pull 및 실행

```bash
# ECR 로그인
aws ecr get-login-password --region ap-northeast-2 | \
docker login --username AWS --password-stdin YOUR_AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-2.amazonaws.com

# 이미지 Pull
docker pull YOUR_AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-2.amazonaws.com/team-it-app:latest

# docker-compose.prod.yml과 .env.prod를 EC2에 복사 후 실행
docker-compose -f docker-compose.prod.yml --env-file .env.prod up -d
```

### 4. 애플리케이션 확인

```bash
# 컨테이너 상태 확인
docker ps

# 로그 확인
docker-compose -f docker-compose.prod.yml logs -f

# Health check
curl http://localhost:8080/actuator/health
```

## ECS/Fargate 배포

### 1. ECS 클러스터 생성

```bash
aws ecs create-cluster --cluster-name team-it-cluster --region ap-northeast-2
```

### 2. Task Definition 생성

`task-definition.json` 파일 생성:

```json
{
  "family": "team-it-task",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::YOUR_ACCOUNT_ID:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "name": "team-it-app",
      "image": "YOUR_AWS_ACCOUNT_ID.dkr.ecr.ap-northeast-2.amazonaws.com/team-it-app:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod"
        }
      ],
      "secrets": [
        {
          "name": "DATABASE_URL",
          "valueFrom": "arn:aws:secretsmanager:region:account-id:secret:database-url"
        },
        {
          "name": "DB_USERNAME",
          "valueFrom": "arn:aws:secretsmanager:region:account-id:secret:db-username"
        },
        {
          "name": "DB_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:region:account-id:secret:db-password"
        },
        {
          "name": "JWT_SECRET",
          "valueFrom": "arn:aws:secretsmanager:region:account-id:secret:jwt-secret"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/team-it",
          "awslogs-region": "ap-northeast-2",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": ["CMD-SHELL", "curl -f http://localhost:8080/actuator/health || exit 1"],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ]
}
```

### 3. Task Definition 등록

```bash
aws ecs register-task-definition --cli-input-json file://task-definition.json
```

### 4. ECS 서비스 생성

```bash
aws ecs create-service \
    --cluster team-it-cluster \
    --service-name team-it-service \
    --task-definition team-it-task \
    --desired-count 2 \
    --launch-type FARGATE \
    --network-configuration "awsvpcConfiguration={subnets=[subnet-xxx,subnet-yyy],securityGroups=[sg-xxx],assignPublicIp=ENABLED}" \
    --load-balancers "targetGroupArn=arn:aws:elasticloadbalancing:...,containerName=team-it-app,containerPort=8080"
```

## 환경 변수 설정

### AWS Secrets Manager 사용 (권장)

```bash
# JWT Secret 저장
aws secretsmanager create-secret \
    --name team-it/jwt-secret \
    --secret-string "your-production-jwt-secret"

# Database credentials 저장
aws secretsmanager create-secret \
    --name team-it/database \
    --secret-string '{
        "url":"jdbc:mysql://your-rds-endpoint:3306/teamit",
        "username":"admin",
        "password":"your-password"
    }'
```

## Application Load Balancer 설정

### 1. ALB 생성

```bash
aws elbv2 create-load-balancer \
    --name team-it-alb \
    --subnets subnet-xxx subnet-yyy \
    --security-groups sg-xxx \
    --scheme internet-facing \
    --type application
```

### 2. Target Group 생성

```bash
aws elbv2 create-target-group \
    --name team-it-tg \
    --protocol HTTP \
    --port 8080 \
    --vpc-id vpc-xxx \
    --health-check-path /actuator/health \
    --health-check-interval-seconds 30 \
    --health-check-timeout-seconds 5 \
    --healthy-threshold-count 2 \
    --unhealthy-threshold-count 3
```

### 3. Listener 생성

```bash
aws elbv2 create-listener \
    --load-balancer-arn arn:aws:elasticloadbalancing:... \
    --protocol HTTP \
    --port 80 \
    --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:...
```

## SSL/TLS 설정 (HTTPS)

### 1. ACM 인증서 발급

```bash
aws acm request-certificate \
    --domain-name yourdomain.com \
    --validation-method DNS \
    --subject-alternative-names "*.yourdomain.com"
```

### 2. HTTPS Listener 추가

```bash
aws elbv2 create-listener \
    --load-balancer-arn arn:aws:elasticloadbalancing:... \
    --protocol HTTPS \
    --port 443 \
    --certificates CertificateArn=arn:aws:acm:... \
    --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:...
```

## 모니터링

### CloudWatch 로그 그룹 생성

```bash
aws logs create-log-group --log-group-name /aws/ecs/team-it
```

### CloudWatch 알람 설정

```bash
# CPU 사용률 알람
aws cloudwatch put-metric-alarm \
    --alarm-name team-it-high-cpu \
    --alarm-description "Alert when CPU exceeds 80%" \
    --metric-name CPUUtilization \
    --namespace AWS/ECS \
    --statistic Average \
    --period 300 \
    --threshold 80 \
    --comparison-operator GreaterThanThreshold \
    --evaluation-periods 2
```

## 유용한 명령어

```bash
# 로그 확인
docker-compose -f docker-compose.prod.yml logs -f

# 컨테이너 재시작
docker-compose -f docker-compose.prod.yml restart

# 컨테이너 중지
docker-compose -f docker-compose.prod.yml down

# 이미지 업데이트 및 재배포
docker-compose -f docker-compose.prod.yml pull
docker-compose -f docker-compose.prod.yml up -d

# ECS 서비스 업데이트 (새 이미지 배포)
aws ecs update-service \
    --cluster team-it-cluster \
    --service team-it-service \
    --force-new-deployment
```

## 보안 체크리스트

- [ ] RDS는 프라이빗 서브넷에 배치
- [ ] 보안 그룹은 최소 권한 원칙 적용
- [ ] SSL/TLS 인증서 적용
- [ ] 환경 변수는 AWS Secrets Manager 사용
- [ ] IAM 역할은 최소 권한으로 설정
- [ ] CloudWatch로 모니터링 및 알람 설정
- [ ] 백업 정책 설정 (RDS 자동 백업)
- [ ] VPC Flow Logs 활성화
- [ ] WAF 적용 (선택사항)

## 트러블슈팅

### 컨테이너가 시작하지 않는 경우

```bash
# 로그 확인
docker logs team-it-app

# Health check 확인
docker inspect team-it-app | grep -A 10 Health
```

### RDS 연결 실패

- 보안 그룹 인바운드 규칙 확인
- RDS 엔드포인트 확인
- VPC 설정 확인

### 메모리 부족

- ECS Task Definition의 메모리 설정 증가
- JVM 힙 메모리 설정 조정 (`-Xmx` 옵션)
