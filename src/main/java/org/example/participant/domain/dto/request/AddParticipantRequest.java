package org.example.participant.domain.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddParticipantRequest {
    private Long userId;
    private Long chatroomId;
}
