package org.example.expert.domain.log.dto.request;

import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogRequest {
	private User user;
	private UserRole newRole;

	public Log toEntity(){
		return new Log(
			this.user,
			this.newRole,
			this.user.getUserRole()
		);
	}
}
