package org.example.reservation.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.ReservationNotFoundException;
import org.example.exception.ReservationNotWaitingException;
import org.example.reservation.domain.dto.CreateReservationRequestDTO;
import org.example.reservation.domain.dto.ReservationDTO;
import org.example.reservation.domain.entity.Reservation;
import org.example.reservation.domain.entity.ReservationStatus;
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



    //전체 에약 조회
    public Page<ReservationDTO> reservationList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Reservation>reservationPage = reservationRepository.findAll(pageable);

        Page<ReservationDTO> reservationDtoList = new ReservationDTO().toDtoList(reservationPage);

        return reservationDtoList;
    }


    //특정 예약 조회
    public ReservationDTO getReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        return ReservationDTO.builder()
                .id(reservation.getId())
                .content(reservation.getContent())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .applyUserName(reservation.getApplyUser().getUsername())
                .receiveUserName(reservation.getReceiveUser().getUsername())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .reservationStatus(reservation.getReservationStatus())
                .build();

    }


    //내 에약들 조회
    public Page<ReservationDTO> getMyReservation(int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ReservationNotFoundException("User not found"));

        Page<Reservation> reservationPage = reservationRepository.findByApplyUser(user, pageable);

        Page<ReservationDTO> reservationDtoList = new ReservationDTO().toDtoList(reservationPage);

        return reservationDtoList;

    }



     //내 신청받은이의 신청응답 대기중인 예약들만 조회
    public Page<ReservationDTO> getWaitingReservation(int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Reservation> reservationPage = reservationRepository.findByReservationStatusEquals(ReservationStatus.PROGRESSING, pageable);

        Page<ReservationDTO> reservationDtoList = new ReservationDTO().toDtoList(reservationPage);

        return reservationDtoList;


    }




    //예약신청

    public void createReservation(CreateReservationRequestDTO createReservationRequestDTO, Long receiveUserId) {

        String applyUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        User applyUser = userRepository.findByUsername(applyUserName)
                .orElseThrow(() -> new ReservationNotFoundException("User not found"));

        User receiveUser = userRepository.findById(receiveUserId)
                .orElseThrow(() -> new ReservationNotFoundException("User not found"));

        Reservation reservation = Reservation.builder()
                .content(createReservationRequestDTO.getContent())
                .startTime(createReservationRequestDTO.getStartTime())
                .endTime(createReservationRequestDTO.getEndTime())
                .reservationStatus(ReservationStatus.PROGRESSING)
                .build();

        reservation.setApplyUser(applyUser);
        reservation.setReceiveUser(receiveUser);

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


    // 신청자가 예약 수정
    public void editReservation(Long reservationId, CreateReservationRequestDTO createReservationRequestDTO){


        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));


        authorizeReservationApplyUser(reservationId);


        if (reservation.getReservationStatus() != ReservationStatus.PROGRESSING) {
            throw new ReservationNotWaitingException("Reservation cannot be edited because it's not in PROGRESSING status");
        }


        reservation.update(createReservationRequestDTO.getContent(), createReservationRequestDTO.getStartTime(), createReservationRequestDTO.getEndTime());


        reservationRepository.save(reservation);

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


















