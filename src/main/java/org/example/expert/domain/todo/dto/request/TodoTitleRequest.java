package org.example.expert.domain.todo.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoTitleRequest {
	private String title;
	private String nickname;
	private LocalDate startDate;
	private LocalDate endDate;

	@Builder
	public TodoTitleRequest(String title, String nickname, LocalDate startDate, LocalDate endDate) {
		this.title = title;
		this.nickname = nickname;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDateTime getStartDateTime() {
		return startDate != null ? startDate.atStartOfDay() : null;
	}

	public LocalDateTime getEndDateTime() {
		return endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
	}

	public static TodoTitleRequest toBuild(Optional<String> title,
		Optional<String> nickname,
		Optional<LocalDate> startDate,
		Optional<LocalDate> endDate) {
		return TodoTitleRequest.builder()
			.title(title.orElse(null))
			.nickname(nickname.orElse(null))
			.startDate(startDate.orElse(null))
			.endDate(endDate.orElse(null))
			.build();
	}
}
