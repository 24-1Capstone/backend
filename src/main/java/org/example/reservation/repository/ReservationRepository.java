package org.example.reservation.repository;

import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.example.user.domain.entity.member.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findAll(Pageable pageable);

    Page<Reservation> findByApplyUser(User applyUser, Pageable pageable);

    Page<Reservation> findByReservationStatusEquals(ReservationStatus status, Pageable pageable);


}
