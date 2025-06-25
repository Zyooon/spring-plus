package org.example.expert.domain.todo.repository;

import java.util.Optional;

import org.example.expert.domain.todo.dto.request.TodoListRequest;
import org.example.expert.domain.todo.dto.request.TodoTitleRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
	Page<Todo> findAllByJPQL(Pageable pageable, TodoListRequest request);
	Optional<Todo> findByIdWithUserQueryDsl(Long todoId);
	Page<Todo> findAllByQueryDsl(Pageable pageable, TodoTitleRequest request);
}
