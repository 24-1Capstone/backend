package org.example.chatroom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.user.domain.entity.BaseEntity;

@Table(name = "chatrooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Chatroom extends BaseEntity {

    // 채팅방id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // 채팅방이름
    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public Chatroom(String name) {
        this.name = name;
    }
}
