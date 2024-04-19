package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.meeting.domain.MeetingSession;
import org.example.meeting.repository.MeetingSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingSessionService {

    private final MeetingSessionRepository meetingSessionRepository;



    public Optional<MeetingSession> findById(Long meetingSessionId){
       return meetingSessionRepository.findById(meetingSessionId);

    }

    public Optional<MeetingSession> findByMeetingId(String meetingId) {
        return meetingSessionRepository.findByMeetingId(meetingId);
    }

    public List<MeetingSession> findAll() {
        return meetingSessionRepository.findAll();
    }
}
