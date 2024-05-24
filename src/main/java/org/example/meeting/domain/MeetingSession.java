package org.example.meeting.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

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
    private String applyUserName;
    private String receiveUserName;


    @Builder
    public MeetingSession(String externalMeetingId, String audioFallbackUrl, String audioHostUrl, String eventIngestionUrl, String screenDataUrl, String screenSharingUrl, String screenViewingUrl, String signalingUrl, String turnControllerUrl, String mediaRegion, String meetingArn, String meetingId, String applyUserName, String receiveUserName) {
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
        this.applyUserName = applyUserName;
        this.receiveUserName = receiveUserName;
    }
}
