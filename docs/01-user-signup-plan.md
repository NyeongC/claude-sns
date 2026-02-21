# User 도메인 & 회원가입/로그인 계획

## User 도메인 필드

| 필드       | 타입     | 설명                    |
|----------|--------|-----------------------|
| id       | Long   | PK, 자동 생성 (IDENTITY)  |
| username | String | 사용자명 (고유, NOT NULL)   |
| password | String | 비밀번호 (Argon2 암호화 저장)  |

## 추가 의존성 (build.gradle)

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
runtimeOnly 'com.h2database:h2'

// Argon2 패스워드 인코딩용
implementation 'org.bouncycastle:bcprov-jdk18on:1.80'
```

## application.yml 설정

```yaml
spring:
  application:
    name: sns-project
  datasource:
    url: jdbc:h2:mem:sns
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true          # /h2-console 접근 가능
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

## API 설계

| 메서드  | URL                    | 설명       | 비고           |
|------|------------------------|----------|--------------|
| GET  | /login                 | 로그인 페이지  | Thymeleaf    |
| GET  | /signup                | 회원가입 페이지 | Thymeleaf    |
| POST | /api/v1/users/signup   | 회원가입 처리  | JSON Request |
| GET  | /                      | 메인 페이지   | 로그인 후 접근     |

## 구현 계획

### 1. Entity

- `User` 엔티티 (`@Entity`, `@Table(name = "users")`)

### 2. Repository

- `UserRepository` (JpaRepository 상속)
- `Optional<User> findByUsername(String username)`

### 3. DTO

- `UserSignupRequest` — username, password (요청용)
- `UserSignupResponse` — id, username (응답용, password 제외)

### 4. Service

- `UserService`
  - username 중복 체크 → 중복 시 예외
  - Argon2PasswordEncoder로 비밀번호 암호화
  - 저장 후 응답 반환

### 5. Spring Security 설정

- `SecurityConfig` (`@Configuration`)
  - Argon2PasswordEncoder Bean 등록
  - 폼 로그인 설정 (loginPage: `/login`, defaultSuccessUrl: `/`)
  - 인메모리 세션 사용 (기본값)
  - 허용 경로: `/login`, `/signup`, `/api/v1/users/signup`, `/h2-console`, 정적 리소스
  - `UserDetailsService` 구현 → DB에서 사용자 조회

### 6. Controller

- `UserApiController` — `POST /api/v1/users/signup` (회원가입 API)
- `PageController` — 페이지 라우팅 (`/login`, `/signup`, `/`)

### 7. Thymeleaf 뷰

- `login.html` — 로그인 폼 (username, password, 로그인 버튼, 회원가입 링크)
- `signup.html` — 회원가입 폼 (username, password, 가입 버튼)
- `index.html` — 메인 페이지 (로그인한 사용자명 표시, 로그아웃 버튼)

## 패키지 구조

```
com.ccn.sns
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── UserApiController.java
│   └── PageController.java
├── domain/
│   └── User.java
├── dto/
│   ├── UserSignupRequest.java
│   └── UserSignupResponse.java
├── repository/
│   └── UserRepository.java
└── service/
    └── UserService.java
```
