package org.example.expert.domain.log.entity;

import org.example.expert.domain.common.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Log extends Timestamped {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long managerId;

	private Long userId;

	private Long todoId;

	public Log(Long userId, Long todoId,  Long managerId) {
		this.userId = userId;
		this.todoId = todoId;
		this.managerId = managerId;
	}
}
