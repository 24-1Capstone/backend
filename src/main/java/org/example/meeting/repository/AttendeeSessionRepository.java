package org.example.meeting.repository;

import org.example.meeting.domain.AttendeeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendeeSessionRepository extends JpaRepository<AttendeeSession, Long> {
    Optional<AttendeeSession> findByExternalUserId(String externalUserId);

    void deleteByMeetingId(String meetingId);

    void deleteByExternalUserId(String externalUserId);


}
