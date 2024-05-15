package org.example.reservation.application;

import lombok.RequiredArgsConstructor;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.example.reservation.domain.entity.TimeSlot;
import org.example.reservation.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    // 특정 시간대에 예약 가능한 시간대 조회
    public List<TimeSlot> findAvailableTimeSlots(LocalDateTime startTime, LocalDateTime endTime) {
        return timeSlotRepository.findByStartTimeBetweenAndReservationStatus(startTime, endTime, ReservationStatus.CONFIRMED);
    }

    public List<TimeSlot> findAllTimeSlots() {
        return timeSlotRepository.findAll();
    }


    public Long createTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        TimeSlot timeSlot = new TimeSlot(startTime, endTime);
        return timeSlotRepository.save(timeSlot).getId();
    }


    public void deleteTimeSlot(Long timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }

    // 특정 시간대에 예약 가능 여부
    public boolean isTimeSlotAvailable(Long timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new TimeSlotNotFoundException("해당 시간대에는 불가" + timeSlotId));
        return timeSlot.isAvailable();
    }

    public class TimeSlotNotFoundException extends RuntimeException {
        public TimeSlotNotFoundException(String message) {
            super(message);
        }
    }
}

