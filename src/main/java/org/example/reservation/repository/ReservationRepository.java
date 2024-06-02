package org.example.reservation.repository;

import org.example.meeting.domain.MeetingSession;
import org.example.reservation.domain.entity.Reservation;
import org.example.user.domain.entity.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT m FROM Reservation m WHERE m.applyUserName = :userName OR m.receiveUserName = :userName")
    List<Reservation> findByApplyUserNameOrReceiveUserName(@Param("userName") String userName);


}
