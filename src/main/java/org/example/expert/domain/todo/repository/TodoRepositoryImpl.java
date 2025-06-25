package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.expert.domain.todo.dto.request.TodoListRequest;
import org.example.expert.domain.todo.dto.request.TodoTitleRequest;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class TodoRepositoryImpl implements TodoRepositoryCustom {

	private final EntityManager entityManager;
	private final JPAQueryFactory queryFactory;

	// 💡 생성자 직접 정의
	public TodoRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<Todo> findAllByJPQL(Pageable pageable, TodoListRequest request) {
		StringBuilder search = new StringBuilder("SELECT t FROM Todo t WHERE 1=1");
		Map<String, Object> params = new HashMap<>();

		if (request.getWeather() != null) {
			search.append(" AND t.weather = :weather");
			params.put("weather", request.getWeather());
		}

		if (request.getStartDateTime() != null && request.getEndDateTime() != null) {
			search.append(" AND t.modifiedAt BETWEEN :start AND :end");
			params.put("start", request.getStartDateTime());
			params.put("end", request.getEndDateTime());
		} else if (request.getStartDateTime() != null) {
			search.append(" AND t.modifiedAt >= :start");
			params.put("start", request.getStartDateTime());
		} else if (request.getEndDateTime() != null) {
			search.append(" AND t.modifiedAt < :end");
			params.put("end", request.getEndDateTime());
		}

		search.append(" ORDER BY t.modifiedAt DESC");

		// 실제 쿼리
		TypedQuery<Todo> query = entityManager.createQuery(search.toString(), Todo.class);
		params.forEach(query::setParameter);
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		List<Todo> content = query.getResultList();

		// 카운트 쿼리
		StringBuilder count = new StringBuilder("SELECT COUNT(t) FROM Todo t WHERE 1=1");
		if (request.getWeather() != null) count.append(" AND t.weather = :weather");
		if (params.containsKey("start") && params.containsKey("end"))
			count.append(" AND t.modifiedAt BETWEEN :start AND :end");
		else if (params.containsKey("start"))
			count.append(" AND t.modifiedAt >= :start");
		else if (params.containsKey("end"))
			count.append(" AND t.modifiedAt < :end");

		TypedQuery<Long> countQuery = entityManager.createQuery(count.toString(), Long.class);
		params.forEach(countQuery::setParameter);
		long total = countQuery.getSingleResult();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Optional<Todo> findByIdWithUserQueryDsl(Long todoId) {
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(todo)
				.leftJoin(todo.user, user).fetchJoin()
				.where(todo.id.eq(todoId))
				.fetchOne()
		);
	}

	@Override
	public Page<Todo> findAllByQueryDsl(Pageable pageable, TodoTitleRequest request) {
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		BooleanBuilder where = new BooleanBuilder();

		if(StringUtils.hasText(request.getTitle())){
			where.and(todo.title.contains(request.getTitle()));
		}
		if(StringUtils.hasText(request.getNickname())){
			where.and(user.nickname.contains(request.getNickname()));
		}

		// 날짜 조건 처리
		LocalDateTime start = request.getStartDateTime();
		LocalDateTime end = request.getEndDateTime();

		if (start != null && end != null) { //시작일 종료일 모두 있을경우
			where.and(todo.createdAt.goe(start));
			where.and(todo.createdAt.lt(end));
		} else if (start != null) {
			where.and(todo.createdAt.goe(start));          // 시작일만 있을 경우 (이상 비교)
		} else if (end != null) {
			where.and(todo.createdAt.lt(end));             // 종료일만 있을 경우 (미만 비교)
		}

		List<Todo> findTodoList = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user, user).fetchJoin()
			.where(where)
			.orderBy(todo.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(
			queryFactory
				.select(todo.count())
				.from(todo)
				.leftJoin(todo.user, user)
				.where(where)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(findTodoList, pageable, total);
	}
}
