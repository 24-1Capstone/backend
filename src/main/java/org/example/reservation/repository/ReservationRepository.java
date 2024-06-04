package org.example.reservation.repository;

import org.example.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByApplyUserUsernameOrReceiveUserUsername(String applyUserName, String receiveUserName);

    void deleteByApplyUserUsernameOrReceiveUserUsername(String applyUserName, String receiveUserName);



}
