package org.example.chatroom.domain.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddChatRequest {
    private String chatroomName;
}