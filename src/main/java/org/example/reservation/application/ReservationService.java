package org.example.reservation.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.ReservationNotFoundException;
import org.example.exception.ReservationNotWaitingException;
import org.example.exception.UserNotFoundException;
import org.example.reservation.domain.dto.CreateReservationRequestDTO;
import org.example.reservation.domain.dto.ReservationDTO;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
import org.example.reservation.repository.ReservationRepository;
import org.example.user.application.member.UserService;
import org.example.user.domain.entity.member.User;
import org.example.user.repository.member.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;



    //내 에약들 조회
    public List<ReservationDTO> getMyReservation() {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Reservation> reservation= reservationRepository.findByApplyUserUsernameOrReceiveUserUsername(userName, userName);

        List<ReservationDTO> reservationDtoList = new ReservationDTO().toDtoList(reservation);

        return reservationDtoList;

    }





    //예약신청

    public void createReservation(CreateReservationRequestDTO createReservationRequestDTO) {

        String applyUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User applyUser = userRepository.findByUsername(applyUserName)
                .orElseThrow(() -> new UserNotFoundException("unexpected user"));

        User receiveUser = userRepository.findByUsername(createReservationRequestDTO.getReceiveUserName())
                .orElseThrow(() -> new UserNotFoundException("unexpected user"));

        Reservation reservation = Reservation.builder()
                .applyUser(applyUser)
                .receiveUser(receiveUser)
                .content(createReservationRequestDTO.getContent())
                .startTime(createReservationRequestDTO.getStartTime())
                .endTime(createReservationRequestDTO.getEndTime())
                .reservationStatus(ReservationStatus.PROGRESSING)
                .receiveUser(receiveUser)
                .build();


        reservationRepository.save(reservation);
    }




    // 신청 받은 유저가 신청 받은 매칭 승인

    public void approveReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        authorizeReservationReceiveUser(reservationId);

        reservation.approve();

        reservationRepository.save(reservation);

    }


    // 신청 받은 유저가 신청 받은 매칭 거절
    public void refuseReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        authorizeReservationReceiveUser(reservationId);

        reservation.refuse();

        reservationRepository.save(reservation);

    }





    // 신청자가 본인 신청 취소
    public void deleteReservation(Long reservationId) {


        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        authorizeReservationApplyUser(reservationId);

        reservationRepository.delete(reservation);

    }

    // 예약삭제
    public void deleteReservationByUserName(String userName) {

        reservationRepository.deleteByApplyUserUsernameOrReceiveUserUsername(userName, userName);

    }






    // 예약 신청한 유저인지 확인
     private void authorizeReservationApplyUser(Long reservationId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

         if (!reservation.getApplyUser().getUsername().equals(userName)) {
             throw new IllegalArgumentException("not authorized");
         }
    }


    // 예약 신청받은 유저인지 확인
    private void authorizeReservationReceiveUser(Long reservationId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!reservation.getReceiveUser().getUsername().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}


















