package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.meeting.domain.MeetingSession;
import org.example.meeting.repository.MeetingSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingSessionService {

    private final MeetingSessionRepository meetingSessionRepository;


    public void save(MeetingSession meetingSession) {
        meetingSessionRepository.save(MeetingSession.builder()
                .externalMeetingId(meetingSession.getExternalMeetingId())
                .mediaRegion(meetingSession.getMediaRegion())
                .meetingArn(meetingSession.getMeetingArn())
                .meetingId(meetingSession.getMeetingId())
                .build());
    }

    public void deleteByMeetingId(String meetingId){
        meetingSessionRepository.deleteByMeetingId(meetingId);
    }

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
