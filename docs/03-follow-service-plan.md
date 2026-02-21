# Follow / FollowCount 서비스 설계 계획

## 개요

사용자 간 팔로우 기능과 팔로워/팔로잉 수 조회 기능을 구현한다.

---

## 공통 BaseEntity

모든 엔티티는 `BaseEntity`를 상속한다.

```java
// domain/BaseEntity.java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt; // soft delete용
}
```

- `@EnableJpaAuditing`은 `SnsProjectApplication`에 설정
- `createdAt`, `updatedAt`은 JPA Auditing으로 자동 관리
- `deletedAt`은 소프트 삭제 시 직접 세팅

---

## 도메인 설계

### Follow 엔티티 (extends BaseEntity)

BaseEntity를 상속하므로 `createdAt`, `updatedAt`, `deletedAt` 컬럼이 자동으로 포함된다.

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| follower_id | Long | FK(users.id) — 팔로우를 거는 사용자 |
| followee_id | Long | FK(users.id) — 팔로우를 받는 사용자 |

- `follower_id`, `followee_id` 쌍에 unique 제약 조건 추가 (중복 팔로우 방지)
- 자기 자신을 팔로우하는 경우 예외 처리

### FollowCount 엔티티 (extends BaseEntity)

팔로워/팔로잉 수를 별도 테이블에 캐싱하여 COUNT 쿼리 없이 빠르게 조회한다.
BaseEntity를 상속하므로 `createdAt`, `updatedAt`, `deletedAt` 컬럼이 자동으로 포함된다.

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| user_id | Long | FK(users.id), unique — 사용자당 1행 |
| followersCount | Long | 나를 팔로우하는 사람 수 |
| followeesCount | Long | 내가 팔로우하는 사람 수 |

- 팔로우/언팔로우 시 `followersCount`, `followeesCount`를 원자적으로 증감한다.
  - `SELECT → 수정 → UPDATE` 방식(갱신 손실 위험) 대신 JPQL로 단일 UPDATE 쿼리를 실행한다.
  - 예: `UPDATE FollowCount SET followersCount = followersCount + 1 WHERE user.id = :userId`
  - 이 쿼리는 DB가 읽기·쓰기를 한 번에 처리하므로 동시 요청이 들어와도 갱신 손실이 발생하지 않는다.

---

## 기능 목록

| 기능 | 메서드 | 설명 |
|------|--------|------|
| 팔로우 | `follow(followerId, followingId)` | Follow 생성 + FollowCount 업데이트 |
| 언팔로우 | `unfollow(followerId, followingId)` | Follow 삭제 + FollowCount 업데이트 |
| 팔로워 수 조회 | `getFollowersCount(userId)` | FollowCount.followersCount 조회 |
| 팔로잉 수 조회 | `getFolloweesCount(userId)` | FollowCount.followeesCount 조회 |
| 팔로워 목록 조회 | `getFollowers(userId)` | 나를 팔로우하는 사람 목록 |
| 팔로잉 목록 조회 | `getFollowees(userId)` | 내가 팔로우하는 사람 목록 |
| 팔로우 여부 확인 | `isFollowing(followerId, followingId)` | 팔로우 관계 존재 여부 |

---

## API 설계

| HTTP 메서드 | URL | 설명 |
|------------|-----|------|
| POST | `/api/v1/follows/{targetUserId}` | 팔로우 (요청자는 쿠키 세션에서 식별) |
| DELETE | `/api/v1/follows/{targetUserId}` | 언팔로우 (요청자는 쿠키 세션에서 식별) |
| GET | `/api/v1/follows/followers` | 내 팔로워 목록 |
| GET | `/api/v1/follows/followees` | 내 팔로잉 목록 |
| GET | `/api/v1/follows/count` | 내 followersCount / followeesCount |

---

## 파일 구조

```
domain/
├── BaseEntity.java              # 공통 상위 엔티티

domain/follow/
├── Follow.java                  # 엔티티 (extends BaseEntity)
├── FollowCount.java             # 엔티티 (extends BaseEntity)
├── FollowRepository.java        # JPA Repository
├── FollowCountRepository.java   # JPA Repository
├── FollowService.java           # 비즈니스 로직
└── FollowException.java         # 예외 (중복 팔로우, 자기 팔로우 등)

controller/
├── FollowApiController.java
└── dto/
    ├── FollowCountResponse.java  # followersCount, followeesCount
    └── FollowUserResponse.java
```

---

## 예외 처리

| 예외 상황 | 처리 방식 |
|-----------|----------|
| 자기 자신 팔로우 | `400 Bad Request` |
| 이미 팔로우한 사용자 팔로우 | `409 Conflict` |
| 팔로우하지 않은 사용자 언팔로우 | `404 Not Found` |
| 존재하지 않는 사용자 | `404 Not Found` |
