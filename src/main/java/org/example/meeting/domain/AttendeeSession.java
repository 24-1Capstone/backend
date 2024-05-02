package org.example.meeting.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendeeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String attendeeId;

    private String externalUserId;

    private String joinToken;

    private String meetingId;




    @Builder
    public AttendeeSession(String attendeeId, String externalUserId, String joinToken, String meetingId) {
        this.attendeeId = attendeeId;
        this.externalUserId = externalUserId;
        this.joinToken = joinToken;
        this.meetingId = meetingId;

    }
}
