package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MediaPlacement {
    private String audioFallbackUrl;
    private String audioHostUrl;
    private String eventIngestionUrl;
    private String screenDataUrl;
    private String screenSharingUrl;
    private String screenViewingUrl;
    private String signalingUrl;
    private String turnControlUrl;


    @Builder
    public MediaPlacement(String audioFallbackUrl, String audioHostUrl, String eventIngestionUrl, String screenDataUrl, String screenSharingUrl, String screenViewingUrl, String signalingUrl, String turnControlUrl) {
        this.audioFallbackUrl = audioFallbackUrl;
        this.audioHostUrl = audioHostUrl;
        this.eventIngestionUrl = eventIngestionUrl;
        this.screenDataUrl = screenDataUrl;
        this.screenSharingUrl = screenSharingUrl;
        this.screenViewingUrl = screenViewingUrl;
        this.signalingUrl = signalingUrl;
        this.turnControlUrl = turnControlUrl;
    }
}
