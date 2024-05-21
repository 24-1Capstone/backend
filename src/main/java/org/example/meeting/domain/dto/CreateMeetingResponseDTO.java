package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateMeetingResponseDTO {

    private String externalMeetingId;
    private MediaPlacement mediaPlacement;
    private String mediaRegion;
    private String meetingArn;
    private String meetingHostId;
    private String meetingId;
    private String primaryMeetingId;



    @Builder
    public CreateMeetingResponseDTO(String externalMeetingId, MediaPlacement mediaPlacement, String mediaRegion, String meetingArn, String meetingHostId, String meetingId, String primaryMeetingId) {
        this.externalMeetingId = externalMeetingId;
        this.mediaPlacement = mediaPlacement;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingHostId = meetingHostId;
        this.meetingId = meetingId;
        this.primaryMeetingId = primaryMeetingId;

    }
}
