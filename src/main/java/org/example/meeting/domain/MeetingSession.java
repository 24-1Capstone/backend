package org.example.meeting.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


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

    private String turnControlUrl;

    private String mediaRegion;

    private String meetingArn;

    private String meetingHostId;

    private String meetingId;

    private String primaryMeetingId;

    @ElementCollection
    @CollectionTable(name = "meeting_tenant_ids")
    private List<String> tenantIds;


    @Builder
    public MeetingSession(Long id, String externalMeetingId, String audioFallbackUrl, String audioHostUrl, String eventIngestionUrl, String screenDataUrl, String screenSharingUrl, String screenViewingUrl, String signalingUrl, String turnControlUrl, String mediaRegion, String meetingArn, String meetingHostId, String meetingId, String primaryMeetingId, List<String> tenantIds) {
        this.id = id;
        this.externalMeetingId = externalMeetingId;
        this.audioFallbackUrl = audioFallbackUrl;
        this.audioHostUrl = audioHostUrl;
        this.eventIngestionUrl = eventIngestionUrl;
        this.screenDataUrl = screenDataUrl;
        this.screenSharingUrl = screenSharingUrl;
        this.screenViewingUrl = screenViewingUrl;
        this.signalingUrl = signalingUrl;
        this.turnControlUrl = turnControlUrl;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingHostId = meetingHostId;
        this.meetingId = meetingId;
        this.primaryMeetingId = primaryMeetingId;
        this.tenantIds = tenantIds;
    }
}
