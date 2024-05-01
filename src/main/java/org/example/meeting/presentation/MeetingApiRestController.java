package org.example.meeting.presentation;


import lombok.RequiredArgsConstructor;
import org.example.meeting.application.ChimeService;
import org.example.meeting.domain.dto.CreateAttendeeResponseDTO;
import org.example.meeting.domain.dto.CreateMeetingResponseDTO;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;




@RestController
@RequiredArgsConstructor
public class MeetingApiRestController {

    private final ChimeService chimeService;
    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;



    //회의 생성

    @PostMapping("/meetings")
    public CreateMeetingResponseDTO createMeeting() {

        return chimeService.createMeetingResponseDTO();

        }




    //참가자 생성
    @PostMapping("/meetings/{meetingId}/attendees")
    public CreateAttendeeResponseDTO createAttendee(@PathVariable String meetingId) {

        return chimeService.createAttendeeResponseDTO(meetingId);
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



    //회의 참가자 조회
    @GetMapping("/meetings/{meetingId}/attendees")
    public ListAttendeesResponse listAttendees(@PathVariable String meetingId,
                                               @RequestParam(required = false) Integer maxResults,
                                               @RequestParam(required = false) String nextToken) {
        ListAttendeesRequest request = ListAttendeesRequest.builder()
                .meetingId(meetingId)
                .maxResults(maxResults)
                .nextToken(nextToken)
                .build();
        return chimeSdkMeetingsClient.listAttendees(request);
    }

}
