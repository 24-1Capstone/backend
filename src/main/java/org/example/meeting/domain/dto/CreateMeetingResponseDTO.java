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
    private String meetingId;
    private String applyUserName;
    private String receiveUserName;


    @Builder
    public CreateMeetingResponseDTO(String externalMeetingId, MediaPlacement mediaPlacement, String mediaRegion, String meetingArn, String meetingId, String applyUserName, String receiveUserName) {
        this.externalMeetingId = externalMeetingId;
        this.mediaPlacement = mediaPlacement;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingId = meetingId;
        this.applyUserName = applyUserName;
        this.receiveUserName = receiveUserName;
    }
}
