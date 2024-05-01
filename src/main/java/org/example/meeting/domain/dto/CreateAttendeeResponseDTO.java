package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateAttendeeResponseDTO {

    private String attendeeId;
    private String externalUserId;
    private String joinToken;


    @Builder
    public CreateAttendeeResponseDTO(String attendeeId, String externalUserId, String joinToken) {
        this.attendeeId = attendeeId;
        this.externalUserId = externalUserId;
        this.joinToken = joinToken;
    }
}
