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
            meetingSessionRepository.save(MeetingSession.builder()
                    .externalMeetingId(meetingSession.getExternalMeetingId())
                    .audioFallbackUrl(meetingSession.getAudioFallbackUrl())
                    .audioHostUrl(meetingSession.getAudioHostUrl())
                    .eventIngestionUrl(meetingSession.getEventIngestionUrl())
                    .screenDataUrl(meetingSession.getScreenDataUrl())
                    .screenSharingUrl(meetingSession.getScreenSharingUrl())
                    .screenViewingUrl(meetingSession.getScreenViewingUrl())
                    .signalingUrl(meetingSession.getSignalingUrl())
                    .turnControllerUrl(meetingSession.getTurnControllerUrl())
                    .mediaRegion(meetingSession.getMediaRegion())
                    .meetingArn(meetingSession.getMeetingArn())
                    .meetingId(meetingSession.getMeetingId())
                    .applyUserName(meetingSession.getApplyUserName())
                    .receiveUserName(meetingSession.getReceiveUserName())
                    .build());
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
        try {
            if (meetingSessionRepository.existsByMeetingId(meetingId)) {
                meetingSessionRepository.deleteByMeetingId(meetingId);
            } else {
                throw new MeetingSessionNotFoundException("Meeting session with ID: " + meetingId + " not found.");
            }
        } catch (Exception e) {
            throw new MeetingSessionDeletionException("Error deleting meeting session with ID: " + meetingId + ": " + e.getMessage());
        }
    }

    public Optional<MeetingSession> findById(Long meetingSessionId) {
        try {
            return meetingSessionRepository.findById(meetingSessionId);
        } catch (Exception e) {
            throw new MeetingSessionNotFoundException("Error finding meeting session with ID: " + meetingSessionId);
        }
    }

    public Optional<MeetingSession> findByMeetingId(String meetingId) {
        try {
            return meetingSessionRepository.findByMeetingId(meetingId);
        } catch (Exception e) {
            throw new MeetingSessionNotFoundException("Error finding meeting session with meeting ID: " + meetingId);
        }
    }

    public List<MeetingSession> findAll() {
        try {
            return meetingSessionRepository.findAll();
        } catch (Exception e) {
            throw new MeetingSessionNotFoundException("Error retrieving all meeting sessions: " + e.getMessage());
        }
    }
}
