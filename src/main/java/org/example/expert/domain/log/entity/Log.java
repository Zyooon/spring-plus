package org.example.expert.domain.log.entity;

import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private UserRole oldRole;

	private UserRole newRole;

	public Log(User user, UserRole oldRole, UserRole newRole) {
		this.user = user;
		this.oldRole = oldRole;
		this.newRole = newRole;
	}

}
