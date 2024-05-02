package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateMeetingWithAttendeesResponseDTO {

    private String externalMeetingId;
    private String mediaRegion;
    private String meetingArn;
    private String meetingId;
    private List<CreateAttendeeResponseDTO> attendees;

    @Builder
    public CreateMeetingWithAttendeesResponseDTO(String externalMeetingId, String mediaRegion, String meetingArn, String meetingId, List<CreateAttendeeResponseDTO> attendees) {
        this.externalMeetingId = externalMeetingId;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingId = meetingId;
        this.attendees = attendees;
    }
}
