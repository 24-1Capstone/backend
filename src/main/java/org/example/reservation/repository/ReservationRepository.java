package org.example.reservation.repository;

import org.example.reservation.domain.entity.Reservation;
import org.example.user.domain.entity.member.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findAll(Pageable pageable);

    Page<Reservation> findByTitleContaining(String keyword, Pageable pageable);

    Page<Reservation> findByContentContaining(String keyword, Pageable pageable);

    Page<Reservation> findByTitleOrContentContaining(String keyword1, String keyword2, Pageable pageable);

    Page<Reservation> findByCreateUserUsernameContaining(String username, Pageable pageable);

    Page<Reservation> findByUser(User user);
}
