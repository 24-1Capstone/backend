package org.example.reservation.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReservationRequestDTO {


    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    @Builder
    public CreateReservationRequestDTO(String content, LocalDateTime startTime, LocalDateTime endTime) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
