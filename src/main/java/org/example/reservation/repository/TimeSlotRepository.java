package org.example.reservation.repository;

import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.example.reservation.domain.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // 주어진 시간 범위에서 예약 가능한 시간대 조회
    List<TimeSlot> findByStartTimeBetweenAndReservationStatus(LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status);
}
