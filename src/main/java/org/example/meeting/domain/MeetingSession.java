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

    private String mediaRegion;

    private String meetingArn;

    private String meetingId;


    @Builder
    public MeetingSession(String externalMeetingId, String mediaRegion, String meetingArn, String meetingId) {
        this.externalMeetingId = externalMeetingId;
        this.mediaRegion = mediaRegion;
        this.meetingArn = meetingArn;
        this.meetingId = meetingId;
    }
}
