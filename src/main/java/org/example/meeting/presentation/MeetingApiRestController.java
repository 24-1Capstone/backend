package org.example.meeting.presentation;


import lombok.RequiredArgsConstructor;
import org.example.meeting.application.ChimeService;
import org.example.meeting.domain.dto.CreateAttendeeResponseDTO;
import org.example.meeting.domain.dto.CreateMeetingResponseDTO;
import org.example.meeting.domain.dto.CreateMeetingWithAttendeesResponseDTO;
import org.example.meeting.domain.dto.ListAttendeesResponseDTO;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class MeetingApiRestController {

    private final ChimeService chimeService;



    //회의 생성
    @PostMapping("/meetings")
    public CreateMeetingResponseDTO createMeeting() {

        return chimeService.createMeetingResponseDTO();

        }



    //해당 회의에 참가자 생성
    @PostMapping("/meetings/{meetingId}/attendees")
    public CreateAttendeeResponseDTO createAttendee(@PathVariable String meetingId) {

        return chimeService.createAttendeeResponseDTO(meetingId);
    }


    //회의 생성 동시에 참가자 생성
    @PostMapping("/meetings/attendees")
    public CreateMeetingWithAttendeesResponseDTO createMeetingWithAttendees(){

        return chimeService.createMeetingWithAttendees();

    }



    //회의 삭제
    @DeleteMapping("/meetings/{meetingId}")
    public void deleteMeeting(@PathVariable String meetingId) {
        chimeService.deleteMeeting(meetingId);
    }

    //참가자 삭제
    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    public void deleteAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        chimeService.deleteAttendee(meetingId, attendeeId);
    }



    //해당 회의의 참가자 조회
    @GetMapping("/meetings/{meetingId}/attendees")
    public ListAttendeesResponseDTO listAttendees(@PathVariable String meetingId) {

        return chimeService.listAttendeesResponseDTO(meetingId);

    }

    // 열려 있는 모든 회의 조회
    @GetMapping("/meetings")
    public List<CreateMeetingResponseDTO> listMeetings(){

        return chimeService.listMeetings();
    }



}
