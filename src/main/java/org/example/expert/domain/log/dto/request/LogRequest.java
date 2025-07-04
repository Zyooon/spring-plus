package org.example.expert.domain.log.dto.request;

import org.example.expert.domain.log.entity.Log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogRequest {
	private Long userId;
	private Long todoId;
	private Long managerUserId;

	public Log toEntity() {
		return new Log(
			this.userId,
			this.todoId,
			this.managerUserId);
	}
}
