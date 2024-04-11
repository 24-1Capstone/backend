package org.example.participant.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.chatroom.domain.entity.Chatroom;
import org.example.user.domain.entity.BaseEntity;
import org.example.user.domain.entity.member.User;

@Table(name = "participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Participant extends BaseEntity {
    // 참여자 id(pk)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // 사용자 id(fk)
    @ManyToOne
    @Column(name = "user_id", nullable = false)
    private User user;

    // 채팅방 id
    @ManyToOne
    @Column(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @Builder
    public Participant(User user, Chatroom chatroom) {
        this.user = user;
        this.chatroom = chatroom;
    }
}
