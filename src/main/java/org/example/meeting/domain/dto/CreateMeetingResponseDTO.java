package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMeetingResponseDTO {

    private String externalMeetingId;
    private String mediaRegion;
    private String meetingArn;
    private String meetingId;


    @Builder
    public CreateMeetingResponseDTO(String externalMeetingId, String mediaRegion, String meetingArn, String meetingId) {
        this.externalMeetingId = externalMeetingId;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingId = meetingId;
    }
}
