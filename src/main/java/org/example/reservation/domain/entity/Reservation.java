package org.example.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.user.domain.entity.member.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private User createUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_user_id")
    private User applyUser;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus reservationStatus;

    @Builder
    public Reservation(User createUser, String title, String content, LocalDateTime startTime, LocalDateTime endTime) {
        this.createUser = createUser;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = ReservationStatus.WAITING;
    }

    //예약 수정
    public void update(String title, String content, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // 예약 신청
    public void apply(User applyUser) {
        this.applyUser = applyUser;
        applyUser.getAppliedReservations().add(this);
        this.reservationStatus = ReservationStatus.PROGRESSING;

    }


    //예약 신청 승인
    public void approve() {

        this.reservationStatus = ReservationStatus.CONFIRMED;
    }


    //연관관계 메소드
    public void setCreateUser(User createUser) {
        this.createUser = createUser;
        createUser.getCreatedReservations().add(this); // 해당 사용자의 생성 예약 목록에 현재 예약 추가
    }

    // 예약을 신청한 사용자 설정
    public void setApplyUser(User applyUser) {
        this.applyUser = applyUser;
        applyUser.getAppliedReservations().add(this); // 해당 사용자의 신청 예약 목록에 현재 예약 추가
    }

}










