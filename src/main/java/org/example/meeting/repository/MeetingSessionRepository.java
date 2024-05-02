package org.example.meeting.repository;

import org.example.meeting.domain.MeetingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingSessionRepository extends JpaRepository<MeetingSession, Long > {

    Optional<MeetingSession> findByMeetingId(String meetingId);

    void deleteByMeetingId(String meetingId);

}
