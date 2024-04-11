package org.example.participant.domain.entity;

import lombok.RequiredArgsConstructor;
import org.example.chatroom.domain.entity.Chatroom;
import org.example.chatroom.domain.entity.ChatroomService;
import org.example.participant.domain.dto.request.AddParticipantRequest;
import org.example.participant.repository.ParticipantRepository;
import org.example.user.application.member.UserService;
import org.example.user.domain.entity.member.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserService userService;
    private final ChatroomService chatroomService;

    @Transactional
    public Long create(AddParticipantRequest dto) {
        User user = userService.findById(dto.getUserId());
        Chatroom chatroom = chatroomService.findById(dto.getChatroomId());
        return participantRepository.save(Participant.builder()
                        .user(user)
                        .chatroom(chatroom)
                        .build())
                .getId();
    }

    public Participant findById(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant Not Found"));
    }

    @Transactional
    public void deleteById(Long participantId) {
        participantRepository.deleteById(participantId);
    }

}
