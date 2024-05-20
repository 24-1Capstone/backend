package org.example.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.reservation.application.ReservationService;
import org.example.reservation.domain.dto.CreateReservationRequestDTO;
import org.example.reservation.domain.dto.ReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;



@Tag(name = "reservation-api-controller", description = "예약 처리 컨트롤러")
@RequiredArgsConstructor
@RestController
public class ReservationApiRestController {

    private final ReservationService reservationService;



    @Operation(summary = "시스템에 등록된 모든 예약 조회", description = "시스템에 등록된 모든 예약 조회")
    @GetMapping("/api/reservation/list")
    public Page<ReservationDTO> reservationList(@RequestParam(value = "page", defaultValue = "0") int page) {

        return reservationService.reservationList(page);

    }

    @Operation(summary = "특정 예약 조회", description = "특정 예약 조회")
    @GetMapping("/api/reservation/{reservationId}")
    public ReservationDTO getReservation(@PathVariable Long reservationId) {

        return reservationService.getReservation(reservationId);

    }




    @Operation(summary = "내 예약들 조회", description = "내 예약들 조회")
    @GetMapping("api/reservation")
    public Page<ReservationDTO> getMyReservation(@RequestParam(defaultValue = "0") int page) {

        return reservationService.getMyReservation(page);

    }


    @Operation(summary = "내 신청받은이의 신청응답 대기중인 예약들만 조회", description = "내 신청받은이의 신청응답 대기중인 예약들만 조회")
    @GetMapping("api/reservation/waiting")
    public Page<ReservationDTO> getWaitingReservation(@RequestParam(defaultValue = "0") int page) {

        return reservationService.getWaitingReservation(page);


    }



    @Operation(summary = "새로운 예약 신청", description = "새로운 예약 신청")
    @PostMapping("/api/reservation/{receiveUserId}")
    public void createReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO, @PathVariable Long receiveUserId) {

        reservationService.createReservation(createReservationRequestDTO, receiveUserId);

    }



    @Operation(summary = "신청받은 매칭 승인", description = "신청받은 매칭 승인")
    @PostMapping("/api/reservation/approve/{reservationId}")
    public void approveReservation(@PathVariable Long reservationId) {

        reservationService.approveReservation(reservationId);

    }

    @Operation(summary = "신청받은 매칭 거절", description = "신청받은 매칭 거절")
    @PostMapping("/api/reservation/refuse/{reservationId}")
    public void refuseReservation(@PathVariable Long reservationId) {

        reservationService.refuseReservation(reservationId);

    }



    @Operation(summary = "작성자가 본인 예약 취소", description = "작성자가 본인 예약 취소")
    @PostMapping("/api/reservation/delete/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId) {

        reservationService.deleteReservation(reservationId);

    }

    @Operation(summary = "신청자 예약서 수정", description = "신청자 예약서 수정")
    @PostMapping("/api/reservation/edit/{reservationId}")
    public void editReservation(@PathVariable Long reservationId, @RequestBody  CreateReservationRequestDTO createReservationRequestDTO){

        reservationService.editReservation(reservationId, createReservationRequestDTO);

    }


}

