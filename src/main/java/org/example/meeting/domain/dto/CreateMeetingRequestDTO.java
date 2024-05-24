package org.example.meeting.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMeetingRequestDTO {
    private String applyUserName;
    private String receiveUserName;


    @Builder
    public CreateMeetingRequestDTO(String applyUserName, String receiveUserName) {
        this.applyUserName = applyUserName;
        this.receiveUserName = receiveUserName;
    }
}
