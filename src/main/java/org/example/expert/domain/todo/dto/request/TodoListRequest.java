package org.example.expert.domain.todo.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoListRequest {
	private String weather;
	private LocalDate startDate;
	private LocalDate endDate;

	@Builder
	public TodoListRequest(String weather, LocalDate startDate, LocalDate endDate) {
		this.weather = weather;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDateTime getStartDateTime() {
		return startDate != null ? startDate.atStartOfDay() : null;
	}

	public LocalDateTime getEndDateTime() {
		return endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
	}

	public static TodoListRequest toBuild(Optional<String> weather,
		Optional<LocalDate> startDate,
		Optional<LocalDate> endDate) {
		return TodoListRequest.builder()
			.weather(weather.orElse(null))
			.startDate(startDate.orElse(null))
			.endDate(endDate.orElse(null))
			.build();
	}
}
