package org.example.meeting.presentation;

import lombok.RequiredArgsConstructor;
import org.example.meeting.application.ChimeService;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;




@RestController
@RequiredArgsConstructor
public class MeetingApiRestController {

    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;
    private final ChimeService chimeService;

    //회의 생성
    @PostMapping("/meetings")

    public CreateMeetingResponse createMeeting(@RequestBody CreateMeetingRequest request){

        return chimeService.createMeetingResponse(request);


    }


    //참가자 생성
    @PostMapping("/meetings/{meetingId}/attendees")
    public CreateAttendeeResponse createAttendee(@PathVariable String meetingId, @RequestBody CreateAttendeeRequest request) {
        CreateAttendeeRequest createAttendeeRequest = CreateAttendeeRequest.builder()
                .meetingId(meetingId)
                .externalUserId(request.getValueForField("externalUserId", String.class).orElse(null))
                .build();
        return chimeSdkMeetingsClient.createAttendee(createAttendeeRequest);
    }

    //회의 삭제
    @DeleteMapping("/meetings/{meetingId}")
    public void deleteMeeting(@PathVariable String meetingId) {
        DeleteMeetingRequest request = DeleteMeetingRequest.builder()
                .meetingId(meetingId)
                .build();
        chimeSdkMeetingsClient.deleteMeeting(request);
    }

    //참가자 삭제
    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    public void deleteAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        DeleteAttendeeRequest request = DeleteAttendeeRequest.builder()
                .meetingId(meetingId)
                .attendeeId(attendeeId)
                .build();
        chimeSdkMeetingsClient.deleteAttendee(request);
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
