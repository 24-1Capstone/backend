package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMeetingResponseDTO {

    private String externalMeetingId;
    private String audioFallbackUrl;
    private String audioHostUrl;
    private String eventIngestionUrl;
    private String screenDataUrl;
    private String screenSharingUrl;
    private String screenViewingUrl;
    private String signalingUrl;
    private String turnControllerUrl;
    private String mediaRegion;
    private String meetingArn;
    private String meetingId;


    @Builder

    public CreateMeetingResponseDTO(String externalMeetingId, String audioFallbackUrl, String audioHostUrl, String eventIngestionUrl, String screenDataUrl, String screenSharingUrl, String screenViewingUrl, String signalingUrl, String turnControllerUrl, String mediaRegion, String meetingArn, String meetingId) {
        this.externalMeetingId = externalMeetingId;
        this.audioFallbackUrl = audioFallbackUrl;
        this.audioHostUrl = audioHostUrl;
        this.eventIngestionUrl = eventIngestionUrl;
        this.screenDataUrl = screenDataUrl;
        this.screenSharingUrl = screenSharingUrl;
        this.screenViewingUrl = screenViewingUrl;
        this.signalingUrl = signalingUrl;
        this.turnControllerUrl = turnControllerUrl;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingId = meetingId;
    }
}
