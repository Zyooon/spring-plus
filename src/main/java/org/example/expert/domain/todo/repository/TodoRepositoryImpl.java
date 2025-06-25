package org.example.expert.domain.todo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.expert.domain.todo.dto.request.TodoListRequest;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
	public Page<Todo> findAllBySearch(Pageable pageable, TodoListRequest request) {
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
}
