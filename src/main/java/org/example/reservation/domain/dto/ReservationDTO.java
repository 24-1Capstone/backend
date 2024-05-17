package org.example.reservation.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class ReservationDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String applyUserName;
    private String receiveUserName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;

    @Builder
    public ReservationDTO(Long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String applyUserName, String receiveUserName, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus reservationStatus) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.applyUserName = applyUserName;
        this.receiveUserName = receiveUserName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
    }

    public ReservationDTO() {
    }

    /* Page<Entity> -> Page<Dto> 변환처리 */
    public Page<ReservationDTO> toDtoList(Page<Reservation> reservationList){
        Page<ReservationDTO> reservationDtoList = reservationList.map(m -> ReservationDTO.builder()
                .id(m.getId())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .applyUserName(m.getApplyUser().getUsername())
                .receiveUserName(m.getReceiveUser().getUsername())
                .startTime(m.getStartTime())
                .endTime(m.getEndTime())
                .reservationStatus(m.getReservationStatus())
                .build());
        return reservationDtoList;
    }

}










