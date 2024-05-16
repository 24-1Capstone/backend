package org.example.reservation.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class ReservationDTO {

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createUsername;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;

    @Builder
    public ReservationDTO(String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String createUsername, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus reservationStatus) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createUsername = createUsername;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
    }

    public ReservationDTO() {
    }

    /* Page<Entity> -> Page<Dto> 변환처리 */
    public Page<ReservationDTO> toDtoList(Page<Reservation> reservationList){
        Page<ReservationDTO> reservationDtoList = reservationList.map(m -> ReservationDTO.builder()
                .title(m.getTitle())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .createUsername(m.getCreateUser().getUsername())
                .startTime(m.getStartTime())
                .endTime(m.getEndTime())
                .reservationStatus(m.getReservationStatus())
                .build());
        return reservationDtoList;
    }

}










