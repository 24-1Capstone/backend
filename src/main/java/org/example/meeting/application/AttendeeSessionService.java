package org.example.meeting.application;
import lombok.RequiredArgsConstructor;
import org.example.exception.AttendeeSessionCreationException;
import org.example.exception.AttendeeSessionNotFoundException;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.repository.AttendeeSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    public void deleteByMeetingId(String meetingId) {

        attendeeSessionRepository.deleteByMeetingId(meetingId);

    }

    public void deleteByExternalUserId(String externalUserId) {

        attendeeSessionRepository.deleteByExternalUserId(externalUserId);

    }

}
