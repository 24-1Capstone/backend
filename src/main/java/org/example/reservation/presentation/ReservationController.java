package org.example.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.exception.ReservationNotFoundException;
import org.example.reservation.application.ReservationService;
import org.example.reservation.domain.dto.CreateReservationRequestDTO;
import org.example.reservation.domain.dto.ReservationDTO;
import org.example.reservation.domain.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "reservation-api-controller", description = "예약 처리 컨트롤러")
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "모든 예약 조회", description = "시스템에 등록된 모든 예약을 조회")
    @GetMapping("/api/reservation/list")
    public Page<ReservationDTO> reservationList(@RequestParam(value= "query", defaultValue = "subject") String query,
                                                @RequestParam(value = "keyword", defaultValue = "") String kw ,
                                                @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<ReservationDTO> reservationList = reservationService.reservationList(query, kw, page);

        return reservationList;
    }




    @Operation(summary = "새로운 예약 생성", description = "새로운 예약을 생성")
    @PostMapping("/api/reservation")
    public void createReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {

        reservationService.createReservation(createReservationRequestDTO);
    }


    @Operation(summary = "해당 예약에 매칭 신청", description = "해당 예약에 매칭 신청")
    @PostMapping("/api/reservation/apply/{reservationId}")
    public void applyReservation(@PathVariable Long reservationId) {

        reservationService.applyReservation(reservationId);

    }



    @Operation(summary = "신청받은 매칭 승인", description = "신청받은 매칭 승인")
    @PostMapping("/api/reservation/approve/{reservationId}")
    public void approveReservation(@PathVariable Long reservationId) {

        reservationService.approveReservation(reservationId);

    }


}

















