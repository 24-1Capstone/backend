package org.example.reservation.presentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.reservation.application.TimeSlotService;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.TimeSlot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "timeslot-api-controller", description = "예약 시간대 구성 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @Operation(summary = "예약 가능한 모든 시간대 조회", description = "예약 가능한 모든 시간대를 조회")
    @GetMapping
    public List<TimeSlot> getAllAvailableTimeSlots(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        return timeSlotService.findAvailableTimeSlots(startTime, endTime);
    }

    @Operation(summary = "새로운 시간대 생성", description = "새로운 예약 가능한 시간대를 생성")
    @PostMapping
    public Long createTimeSlot(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        return timeSlotService.createTimeSlot(startTime, endTime);
    }

    @Operation(summary = "시간대 삭제", description = "해당 ID의 예약 시간대를 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeSlot(@PathVariable Long id) {
        try {
            timeSlotService.deleteTimeSlot(id);
            return ResponseEntity.ok().build();
        } catch (TimeSlotService.TimeSlotNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 예외 처리
    @ExceptionHandler(TimeSlotService.TimeSlotNotFoundException.class)
    public ResponseEntity<String> handleTimeSlotNotFoundException(TimeSlotService.TimeSlotNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
