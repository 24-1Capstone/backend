package org.example.reservation.domain.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReservationDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
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
        this.applyUserName = applyUserName;
        this.receiveUserName = receiveUserName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
    }

    public ReservationDTO() {
    }


    public List<ReservationDTO> toDtoList(List<Reservation> reservationList) {
        return reservationList.stream()
                .map(m -> ReservationDTO.builder()
                        .id(m.getId())
                        .content(m.getContent())
                        .createdAt(m.getCreatedAt())
                        .applyUserName(m.getApplyUser().getUsername())
                        .receiveUserName(m.getReceiveUser().getUsername())
                        .startTime(m.getStartTime())
                        .endTime(m.getEndTime())
                        .reservationStatus(m.getReservationStatus())
                        .build())
                .collect(Collectors.toList());
    }


}










