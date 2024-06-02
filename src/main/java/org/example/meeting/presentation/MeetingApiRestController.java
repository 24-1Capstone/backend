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
    @PostMapping("/api/meetings")
    public CreateMeetingResponseDTO createMeeting(@RequestBody CreateMeetingRequestDTO createMeetingRequestDTO) {

       return chimeService.createMeeting(createMeetingRequestDTO.getApplyUserName(), createMeetingRequestDTO.getReceiveUserName());
    }


    @Operation(summary = "해당 회의에 참가자 생성", description = "특정 회의에 참가자를 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "참가자 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @PostMapping("/api/meetings/{meetingId}/attendees")
    public CreateAttendeeResponseDTO createAttendee(@PathVariable String meetingId) {
        return chimeService.createAttendee(meetingId);
    }




    @Operation(summary = "회의 삭제 API", description = "특정 회의를 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회의 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "입력 매개변수가 서비스 제한과 일치하지 않음"),
            @ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @DeleteMapping("/api/meetings/{meetingId}")
    public void deleteMeeting(@PathVariable String meetingId) {

        chimeService.deleteMeeting(meetingId);

    }





    @Operation(summary = "열려 있는 모든 회의 조회 API", description = "현재 열려 있는 모든 회의를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회의 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서비스가 예기치 않은 오류를 만남")
    })
    @GetMapping("/api/meetings")
    public List<CreateMeetingResponseDTO> listMeetings() {

        return chimeService.listMeetings();
    }


}