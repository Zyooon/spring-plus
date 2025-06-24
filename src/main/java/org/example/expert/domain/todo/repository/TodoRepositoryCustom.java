package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoListRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
	Page<Todo> findAllBySearch(Pageable pageable, TodoListRequest request);
}
