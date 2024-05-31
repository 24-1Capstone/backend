package org.example.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.reservation.application.ReservationService;
import org.example.reservation.domain.dto.CreateReservationRequestDTO;
import org.example.reservation.domain.dto.ReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "reservation-api-controller", description = "예약 처리 컨트롤러")
@RequiredArgsConstructor
@RestController
public class ReservationApiRestController {

    private final ReservationService reservationService;




    @Operation(summary = "내 예약들 조회", description = "내 예약들 조회")
    @GetMapping("api/reservation")
    public List<ReservationDTO> getMyReservation() {

        return reservationService.getMyReservation();

    }




    @Operation(summary = "새로운 예약 신청", description = "새로운 예약 신청")
    @PostMapping("/api/reservation/")
    public void createReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {

        reservationService.createReservation(createReservationRequestDTO);

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




}

