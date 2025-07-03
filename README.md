# Spring Plus

Spring Plus는 기존에 제공된 Spring Plus 코드에 기능을 추가하고 구조를 개선한 프로젝트입니다.

학습 목적의 실습을 통해 백엔드 개발에 필요한 핵심 기술들을 적용하고 테스트 중심의 안정적인 코드를 구현하는 데 집중했습니다.

## 🔧 수정 사항 (총 11개)

1. **`/todos` 호출 시 에러 발생 수정**
    - `TodoService` 클래스 내 `@Transactional(readOnly = true)` 설정을 제거하여 쓰기 작업이 가능하도록 수정
    
2. **JWT에 닉네임 정보 추가**
    - `User` 엔티티에 `nickname` 필드 추가
    - 회원가입 시 닉네임 입력 받도록 요청 필드 확장
    - 로그인 시 생성되는 JWT에 닉네임 포함되도록 수정
    
3. **Todo 검색 조건 확장**
    - 검색 조건에 `날씨(weather)` 필드 추가
    - `시작일 ~ 종료일` 기간 조건 추가
    - JPQL을 이용한 동적 쿼리 구현
    
4. **TodoController 테스트 코드 수정**
    - `todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다()` 부분 수정
    - `status().isOk()` → `status().is4xxClientError()`로 수정
    
5. **AOP 로그 기능 개선**
    - `UserAdminController`의 `changeUserRole()` 메서드 실행 **전(before)** 로그 기록되도록 변경
    - 포인트컷 위치 명확히 지정
    - AOP 어노테이션 수정: `@After` → `@Before`
    
6. **Todo 엔티티 연관관계 설정 보완**
    - `Todo` ↔ `Manager(User)` 연관관계에 `CascadeType.PERSIST` 추가
    - Todo 생성 시, 현재 사용자(User)가 자동으로 담당자로 등록되도록 구현
    
7. **`CommentRepository`의 N+1 문제 해결**
    - `findByTodoIdWithUser()` 쿼리에 `JOIN FETCH` 추가하여 N+1 문제 방지
    
8. **`TodoRepository` QueryDSL 적용**
    - 기존 JPA 방식 → QueryDSL 기반으로 전환
    - `TodoRepositoryCustom`, `TodoRepositoryImpl` 클래스 추가
    - `findByIdWithUser()` → `findByIdWithUserQueryDsl()`로 변경
    
9. **Spring Security 적용**
    - Spring Security 기반 인증 처리 도입
    - 커스텀 필터에서 `SecurityContextHolder`에 인증 객체 저장
    - 컨트롤러 메서드에서 `@AuthenticationPrincipal AuthUser`를 통해 사용자 정보 주입받도록 구현
    
10. **조건 기반 Todo 제목 리스트 API 추가**
    - Todo 제목, 담당자 수, 댓글 수를 포함한 요약 리스트 반환 API 구현
    - QueryDSL을 사용해 성능 최적화
    
11. **매니저 등록 시 로그 생성 기능 추가**
    - `Log` 엔티티 생성 및 로그 저장 기능 구현
    - 로그 저장 로직에 `@Transactional(propagation = REQUIRES_NEW)` 적용하여
        
        주 비즈니스 트랜잭션과 분리 실행되도록 처리