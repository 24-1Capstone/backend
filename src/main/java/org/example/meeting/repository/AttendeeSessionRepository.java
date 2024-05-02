package org.example.meeting.repository;

import org.example.meeting.domain.AttendeeSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendeeSessionRepository extends JpaRepository<AttendeeSession, Long> {

    Optional<AttendeeSession> findByAttendeeId(String attendeeId);

    Optional<AttendeeSession> findByJoinToken(String attendeeId);

    void deleteByAttendeeId(String attendeeId);

    void deleteByMeetingId(String meetingId);
}
