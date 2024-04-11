package org.example.chatroom.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddChatRequest {
    private String chatroomName;
}