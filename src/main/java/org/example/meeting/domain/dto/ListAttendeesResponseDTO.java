package org.example.meeting.domain.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ListAttendeesResponseDTO {

    private List<CreateAttendeeResponseDTO> attendees;

    @Builder
    public ListAttendeesResponseDTO(List<CreateAttendeeResponseDTO> attendees) {
        this.attendees = attendees;

    }
}
