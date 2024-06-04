package org.example.meeting.repository;

import org.example.meeting.domain.MeetingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MeetingSessionRepository extends JpaRepository<MeetingSession, Long > {

    Optional<MeetingSession> findByMeetingId(String meetingId);

    @Query("SELECT m FROM MeetingSession m WHERE m.applyUserName = :userName OR m.receiveUserName = :userName")
    List<MeetingSession> findByApplyUserNameOrReceiveUserName(@Param("userName") String userName);

    void deleteByMeetingId(String meetingId);






}
