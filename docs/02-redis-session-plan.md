# Redis 공용 세션 저장소 연동 계획

## 목적

현재 세션은 JVM 메모리에 저장 → 서버 재시작 시 세션 소멸, 다중 서버 환경에서 세션 공유 불가.
Redis를 공용 세션 저장소로 사용하여 이 문제를 해결한다.

## 추가 의존성 (build.gradle)

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'org.springframework.session:spring-session-data-redis'
```

## application.yml 추가 설정

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  session:
    store-type: redis
    redis:
      namespace: sns:session
```

## Docker Compose (docker-compose.yml)

```yaml
services:
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    command: redis-server --appendonly yes

volumes:
  redis-data:
```

## 구현 계획

### 1. build.gradle
- `spring-boot-starter-data-redis` 추가
- `spring-session-data-redis` 추가

### 2. application.yml
- Redis 연결 정보 (host, port) 추가
- `spring.session.store-type: redis` 설정
- 세션 네임스페이스 `sns:session` 지정

### 3. docker-compose.yml 생성
- Redis 7 Alpine 이미지 사용
- 6379 포트 노출
- `appendonly yes` 로 AOF 영속성 활성화
- named volume으로 데이터 유지

### 4. SecurityConfig.java
- 별도 코드 변경 없음 (Spring Session이 자동으로 세션 저장소를 교체)

## 변경 후 세션 흐름

```
로그인 성공
  → Spring Session이 세션 생성
    → JVM 메모리 대신 Redis에 저장 (sns:session:{sessionId})
      → 브라우저에 JSESSIONID 쿠키 발급

이후 요청
  → JSESSIONID 쿠키
    → Redis에서 세션 조회
      → SecurityContext 복원 → 인증 유지
```

## 실행 방법

```bash
# Redis 컨테이너 시작
docker compose up -d

# 애플리케이션 시작
./gradlew bootRun
```
