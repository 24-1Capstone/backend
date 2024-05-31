package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.AttendeeSessionCreationException;
import org.example.exception.AttendeeSessionDeletionException;
import org.example.exception.AttendeeSessionNotFoundException;
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

    public void save(AttendeeSession attendeeSession) {
        try {
            attendeeSessionRepository.save(AttendeeSession.builder()
                    .attendeeId(attendeeSession.getAttendeeId())
                    .externalUserId(attendeeSession.getExternalUserId())
                    .joinToken(attendeeSession.getJoinToken())
                    .meetingId(attendeeSession.getMeetingId())
                    .build());
        } catch (Exception e) {
            throw new AttendeeSessionCreationException("Error saving attendee session");
        }
    }

    public Optional<AttendeeSession> findByExternalUserId(String externalUserId) {
        try {
            return attendeeSessionRepository.findByExternalUserId(externalUserId);
        } catch (Exception e) {
            throw new AttendeeSessionNotFoundException("Error finding attendee session by external user ID: " + externalUserId);
        }
    }

    public Optional<AttendeeSession> findById(Long attendeeSessionId) {
        try {
            return attendeeSessionRepository.findById(attendeeSessionId);
        } catch (Exception e) {
            throw new AttendeeSessionNotFoundException("Error finding attendee session by ID: " + attendeeSessionId);
        }
    }

    public void deleteByAttendeeId(String attendeeId) {
        try {
            if (attendeeSessionRepository.existsByMeetingId(attendeeId)) {
                attendeeSessionRepository.deleteByAttendeeId(attendeeId);
            } else {
                throw new AttendeeSessionNotFoundException("Attendee session with ID: " + attendeeId + " not found.");
            }
        } catch (Exception e) {
            throw new AttendeeSessionDeletionException("Error deleting attendee session with ID: " + attendeeId);
        }
    }

    public void deleteByMeetingId(String meetingId) {

        attendeeSessionRepository.deleteByMeetingId(meetingId);

    }

    public Optional<AttendeeSession> findByAttendeeId(String attendeeId) {
        try {
            return attendeeSessionRepository.findByAttendeeId(attendeeId);
        } catch (Exception e) {
            throw new AttendeeSessionNotFoundException("Error finding attendee session by ID: " + attendeeId);
        }
    }

    public Optional<AttendeeSession> findByJoinToken(String joinToken) {
        try {
            return attendeeSessionRepository.findByJoinToken(joinToken);
        } catch (Exception e) {
            throw new AttendeeSessionNotFoundException("Error finding attendee session by join token: " + joinToken);
        }
    }

    public List<AttendeeSession> findAll() {
        try {
            return attendeeSessionRepository.findAll();
        } catch (Exception e) {
            throw new AttendeeSessionNotFoundException("Error retrieving all attendee sessions");
        }
    }
}
