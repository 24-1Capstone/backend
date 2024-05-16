package org.example.reservation.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.ReservationNotFoundException;
import org.example.reservation.domain.dto.CreateReservationRequestDTO;
import org.example.reservation.domain.dto.ReservationDTO;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.repository.ReservationRepository;
import org.example.user.domain.entity.member.User;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;



    //전체 에약 조회 및 특정 예약 검색(-query: 제목검색, 사용자로 검색, 제목+내용 으로 검색, -kw: 검색내용)
    public Page<ReservationDTO> reservationList(String query, String kw, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Reservation> reservationPage = null;

        if ("subject".equals(query)) {
            reservationPage = reservationRepository.findByTitleContaining(kw, pageable);
        } else if ("content".equals(query)) {
            reservationPage = reservationRepository.findByContentContaining(kw, pageable);
        } else if ("user".equals(query)) {
            reservationPage = reservationRepository.findByCreateUserUsernameContaining(kw, pageable);
        } else if ("subject+content".equals(query)) {
            reservationPage = reservationRepository.findByTitleOrContentContaining(kw, kw, pageable);
        } else {
            reservationPage = reservationRepository.findAll(pageable);
        }

        Page<ReservationDTO> reservationDtoList = new ReservationDTO().toDtoList(reservationPage);

        return reservationDtoList;
    }


    //내 에약들 조회
    public Page<ReservationDTO> getReservation(int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        Page<Reservation> reservationPage = reservationRepository.findByUser(user);

        Page<ReservationDTO> reservationDtoList = new ReservationDTO().toDtoList(reservationPage);

        return reservationDtoList;

    }





    //예약신청서 생성

    public void createReservation(CreateReservationRequestDTO createReservationRequestDTO) {

        String createUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User createUser = userRepository.findByUsername(createUserName).orElse(null);

        Reservation reservation = Reservation.builder()
                .createUser(createUser)
                .title(createReservationRequestDTO.getTitle())
                .content(createReservationRequestDTO.getContent())
                .startTime(createReservationRequestDTO.getStartTime())
                .endTime(createReservationRequestDTO.getEndTime())
                .build();

        reservationRepository.save(reservation);
    }

    //매칭 신청

    public void applyReservation(Long reservationId) {
        String applyUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(applyUserName).orElse(null);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 신청이 불가능합니다."));

        reservation.apply(user);

        reservationRepository.save(reservation);
    }


    // 작성자가 신청된 매칭 승인

    public void approveReservation(Long reservationId) {

        authorizeReservationAuthor(reservationId);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        reservation.approve();

        reservationRepository.save(reservation);

    }


    // 작성자가 본인 예약 취소
    public void deleteReservation(Long reservationId) {

        authorizeReservationAuthor(reservationId);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        reservationRepository.delete(reservation);

    }




    // 예약신청서 작성한 유저인지 확인
     private void authorizeReservationAuthor(Long reservationId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

         if (!reservation.getCreateUser().getUsername().equals(userName)) {
             throw new IllegalArgumentException("not authorized");
         }
    }

}


















