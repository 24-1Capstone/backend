package org.example.chatroom.repository;

import org.example.chatroom.domain.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    List<Chatroom> findByChatroomName(String chatroomName);
}
