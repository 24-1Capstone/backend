package org.example.meeting.application;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.repository.AttendeeSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendeeSessionService {

    private final AttendeeSessionRepository attendeeSessionRepository;

    public Optional<AttendeeSession> findById(Long attendeeSessionId) {
        return attendeeSessionRepository.findById(attendeeSessionId);

    }

    public Optional<AttendeeSession> findByAttendeeId(String attendeeId){
        return attendeeSessionRepository.findByAttendeeId(attendeeId);
    }

    public Optional<AttendeeSession> findByJoinToken(String joinToken) {
        return attendeeSessionRepository.findByJoinToken(joinToken);
    }

    public List<AttendeeSession> findAll() {
        return attendeeSessionRepository.findAll();
    }


}
