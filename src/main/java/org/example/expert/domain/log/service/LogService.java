package org.example.expert.domain.log.service;
import static org.springframework.transaction.annotation.Propagation.*;

import org.example.expert.domain.log.dto.request.LogRequest;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
	private final LogRepository logRepository;

	@Transactional(propagation = REQUIRES_NEW)
	public void saveLog(LogRequest request){
		Log log = request.toEntity();
		logRepository.save(log);
	}
}
