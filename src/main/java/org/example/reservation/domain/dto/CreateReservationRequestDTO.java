package org.example.reservation.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReservationRequestDTO {


    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String receiveUserName;


    @Builder
    public CreateReservationRequestDTO(String content, LocalDateTime startTime, LocalDateTime endTime, String receiveUserName) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.receiveUserName = receiveUserName;
    }
}
