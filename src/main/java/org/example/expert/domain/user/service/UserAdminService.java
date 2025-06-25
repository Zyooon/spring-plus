package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.log.dto.request.LogRequest;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final LogService logService;

    @Transactional
    public void changeUserRole(long userId, UserRoleChangeRequest userRoleChangeRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));

        UserRole newRole = UserRole.of(userRoleChangeRequest.getRole());

        //로그 생성
        logService.saveLog(new LogRequest(user, newRole));
        try {
            logService.saveLog(new LogRequest(user, newRole));
        } catch (Exception e) {
            throw new IllegalStateException("로그 저장 에러 발생", e);
        }

        user.updateRole(newRole);
    }
}
