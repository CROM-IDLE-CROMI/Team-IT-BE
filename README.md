# Team-IT-BE

## 1\. 프로젝트 소개

**Team-IT**는 IT 프로젝트, 공모전 등 다양한 IT 관련 팀활동을 위한 팀원들을 모집하고 지원할 수 있는 협업 플랫폼 백엔드 서버입니다. 사용자는 프로젝트를 등록하여 팀원을 모집하거나, 관심 있는 프로젝트에 지원하여 팀에 합류할 수 있습니다. 본 프로젝트는 Spring Boot 기반의 RESTful API 서버로, 안전한 사용자 인증과 효율적인 데이터 관리를 목표로 합니다.

## 2\. 주요 기능

* **사용자 인증**:

    * **회원가입**: 아이디(UID), 이메일 중복 검사를 포함한 사용자 등록 기능입니다.
    * **로그인**: Spring Security와 JWT를 이용한 안전한 로그인 기능입니다. 인증 성공 시 Access Token과 Refresh Token을 발급합니다.
    * **로그아웃**: 서버에 저장된 사용자의 Refresh Token을 삭제하여 세션을 무효화합니다.
    * **토큰 재발급**: 만료된 Access Token을 유효한 Refresh Token을 통해 재발급합니다.

* **팀 프로젝트 관리**:

    * **프로젝트 생성**: 플랫폼, 모집 직군, 기술 스택, 활동 종류 등 상세한 정보를 포함하여 새로운 팀 프로젝트를 등록할 수 있습니다.
    * **프로젝트 상세 조회**: 특정 프로젝트의 모든 상세 정보와 작성자 프로필을 조회할 수 있으며, 조회 시 조회수가 1 증가합니다.

* **프로젝트 지원 및 참여**:

    * **프로젝트 지원**: 관심 있는 프로젝트에 지원 동기, 질문에 대한 답변 등을 포함한 지원서를 제출할 수 있습니다.
    * **나의 프로젝트 조회**: 사용자가 참여하고 있는 '진행 중인 프로젝트'와 '완료된 프로젝트' 목록을 필터링하여 조회할 수 있습니다.

## 3\. 기술 스택

* **Framework**: Spring Boot 3.5.0
* **Language**: Java 21
* **Security**: Spring Security, JWT (JSON Web Token)
* **Database**: MySQL, Spring Data JPA
* **Build Tool**: Gradle
* **Library**: Lombok, JJWT (for JWT), Hibernate Validator

## 4\. API Endpoints

주요 API 엔드포인트는 다음과 같습니다.

| Method | URI                                | 설명                       |
| :----- | :--------------------------------- | :------------------------- |
| POST   | `/v1/auth/users`                   | 회원가입                   |
| POST   | `/v1/auth/login`                   | 로그인 (토큰 발급)         |
| POST   | `/v1/auth/refresh`                 | 토큰 재발급                |
| POST   | `/v1/auth/logout`                  | 로그아웃                   |
| POST   | `/v1/teams`                        | 새 프로젝트(팀) 생성       |
| GET    | `/v1/projects/{projectId}`         | 프로젝트 상세 정보 조회    |
| GET    | `/v1/projects/{projectId}/submission` | 프로젝트 지원 페이지 정보 조회 |
| POST   | `/v1/projects/{projectId}/apply`   | 프로젝트 지원서 제출       |
| GET    | `/v1/my-projects/{uid}`            | 나의 모든 프로젝트 조회    |