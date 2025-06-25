package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoTitleResponse {
	private final String title;
	private final int managerCount;
	private final int commentCount;

}
