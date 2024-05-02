package org.example.meeting.presentation;

import lombok.RequiredArgsConstructor;
import org.example.meeting.application.ChimeService;
import org.example.meeting.domain.dto.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Tag(name = "chime-meeting-restapi", description = "Chime 관련 RESTAPI")
public class MeetingApiRestController {

    private final ChimeService chimeService;

    @Operation(summary = "회의 생성", description = "새로운 회의를 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회의 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @PostMapping("/meetings")
    public CreateMeetingResponseDTO createMeeting() {
        return chimeService.createMeetingResponseDTO();
    }



    @Operation(summary = "해당 회의에 참가자 생성", description = "특정 회의에 참가자를 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "참가자 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @PostMapping("/meetings/{meetingId}/attendees")
    public CreateAttendeeResponseDTO createAttendee(@PathVariable String meetingId) {
        return chimeService.createAttendeeResponseDTO(meetingId);
    }


    @Operation(summary = "회의 생성 동시에 참가자 생성 API", description = "회의를 생성하면서 동시에 참가자를 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회의 및 참가자 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @PostMapping("/meetings/attendees")
    public CreateMeetingWithAttendeesResponseDTO createMeetingWithAttendees() {
        return chimeService.createMeetingWithAttendees();
    }


    @Operation(summary = "회의 삭제 API", description = "특정 회의를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회의 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @DeleteMapping("/meetings/{meetingId}")
    public void deleteMeeting(@PathVariable String meetingId) {
        chimeService.deleteMeeting(meetingId);
    }


    @Operation(summary = "참가자 삭제 API", description = "특정 회의의 특정 참가자를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "참가자 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @DeleteMapping("/meetings/{meetingId}/attendees/{attendeeId}")
    public void deleteAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        chimeService.deleteAttendee(meetingId, attendeeId);
    }


    @Operation(summary = "특정 회의의 참가자 조회 API", description = "특정 회의의 참가자 목록을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참가자 조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @GetMapping("/meetings/{meetingId}/attendees")
    public ListAttendeesResponseDTO listAttendees(@PathVariable String meetingId) {
        return chimeService.listAttendeesResponseDTO(meetingId);
    }


    @Operation(summary = "열려 있는 모든 회의 조회 API", description = "현재 열려 있는 모든 회의를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회의 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @GetMapping("/meetings")
    public List<CreateMeetingResponseDTO> listMeetings() {
        return chimeService.listMeetings();
    }
}
