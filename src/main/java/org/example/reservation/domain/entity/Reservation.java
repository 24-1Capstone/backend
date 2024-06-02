package org.example.reservation.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.user.domain.entity.member.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;


    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_user_id")
    private User applyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private User receiveUser;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus reservationStatus;

    @Builder
    public Reservation(User applyUser, User receiveUser, String content, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus reservationStatus) {
        this.applyUser = applyUser;
        this.receiveUser = receiveUser;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
    }



    //예약 신청 승인
    public void approve() {

        this.reservationStatus = ReservationStatus.CONFIRMED;
    }


    //예약 신청 거절
    public void refuse() {

        this.reservationStatus = ReservationStatus.REFUSE;

    }



}










