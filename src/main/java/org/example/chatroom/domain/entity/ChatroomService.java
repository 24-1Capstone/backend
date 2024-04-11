package org.example.chatroom.domain.entity;

import lombok.RequiredArgsConstructor;
import org.example.chatroom.domain.dto.request.AddChatRequest;
import org.example.chatroom.repository.ChatroomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    @Transactional
    public Long create(AddChatRequest dto){
        return chatroomRepository.save(Chatroom.builder()
                        .name(dto.getChatroomName())
                .build()).getId();
    }

    public Chatroom findById(Long chatroomId){
        return chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("Chatroom not found"));
    }

    public List<Chatroom> findByChatroomName(String chatroomName){
        return chatroomRepository.findByChatroomName(chatroomName);
    }
    @Transactional
    public void deleteById(Long chatroomId){
        chatroomRepository.deleteById(chatroomId);
    }
}
