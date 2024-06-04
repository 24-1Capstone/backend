package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.MeetingSessionCreationException;
import org.example.exception.MeetingSessionDeletionException;
import org.example.exception.MeetingSessionNotFoundException;
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
        try {
            meetingSessionRepository.save(meetingSession);

        } catch (Exception e) {
            throw new MeetingSessionCreationException("Error saving meeting session: " + e.getMessage());
        }
    }

    public List<MeetingSession> listMeetings(String userName) {
        try {
            return meetingSessionRepository.findByApplyUserNameOrReceiveUserName(userName);
        } catch (Exception e) {
            throw new MeetingSessionNotFoundException("Error listing meeting sessions for user: " + userName);
        }
    }

    public void deleteByMeetingId(String meetingId) {
        Optional<MeetingSession> meetingSessionOptional = meetingSessionRepository.findByMeetingId(meetingId);
        if (meetingSessionOptional.isPresent()) {
            meetingSessionRepository.deleteByMeetingId(meetingId);
        } else {
            throw new MeetingSessionNotFoundException("Meeting session with ID: " + meetingId + " not found.");
        }
    }

    public void deleteByMeetingSessionId(Long meetingSessionId) {
        Optional<MeetingSession> meetingSessionOptional = meetingSessionRepository.findById(meetingSessionId);
        if (meetingSessionOptional.isPresent()) {
            meetingSessionRepository.deleteById(meetingSessionId);
        } else {
            throw new MeetingSessionNotFoundException("Meeting session with ID: " + meetingSessionId + " not found.");
        }
    }



    public Optional<MeetingSession> findByMeetingId(String meetingId) {
        try {
            return meetingSessionRepository.findByMeetingId(meetingId);
        } catch (Exception e) {
            throw new MeetingSessionNotFoundException("Error finding meeting session with meeting ID: " + meetingId);
        }
    }


}
