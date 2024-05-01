package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.domain.dto.CreateAttendeeResponseDTO;
import org.example.meeting.domain.dto.CreateMeetingResponseDTO;
import org.example.meeting.domain.MeetingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChimeService {

    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;
    private final MeetingSessionService meetingSessionService;
    private final AttendeeSessionService attendeeSessionService;



    // MeetingSession 엔티티에 저장 및 createMeetingResponseDTO 반환
    public CreateMeetingResponseDTO createMeetingResponseDTO(){
        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .clientRequestToken("abc123")
                .externalMeetingId("meeting123")
                .mediaRegion("ap-northeast-2")
                .build();

    CreateMeetingResponse createMeetingResponse = chimeSdkMeetingsClient.createMeeting(request);

    String externalMeetingId = createMeetingResponse.meeting().externalMeetingId();
    String mediaRegion = createMeetingResponse.meeting().mediaRegion();
    String metingArn = createMeetingResponse.meeting().meetingArn();
    String meetingId = createMeetingResponse.meeting().meetingId();

    CreateMeetingResponseDTO createMeetingResponseDTO = CreateMeetingResponseDTO.builder()
            .externalMeetingId(externalMeetingId)
            .mediaRegion(mediaRegion)
            .meetingArn(metingArn)
            .meetingId(meetingId)
            .build();

    MeetingSession meetingSession = MeetingSession.builder()
            .externalMeetingId(externalMeetingId)
            .mediaRegion(mediaRegion)
            .meetingArn(metingArn)
            .meetingId(meetingId)
            .build();


    meetingSessionService.save(meetingSession);




        return createMeetingResponseDTO;

}


    // AttendeeSession 엔티티에 저장 및 createAttendeeResponseDTO 반환
    public CreateAttendeeResponseDTO createAttendeeResponseDTO(String meetingID){

        CreateAttendeeRequest request = CreateAttendeeRequest.builder()
                .meetingId(meetingID)
                .externalUserId("meeting122144")
                .build();



         CreateAttendeeResponse createAttendeeResponse = chimeSdkMeetingsClient.createAttendee(request);


        String attendeeId = createAttendeeResponse.attendee().attendeeId();
        String externalUserId = createAttendeeResponse.attendee().externalUserId();
        String joinToken = createAttendeeResponse.attendee().joinToken();



        CreateAttendeeResponseDTO createAttendeeResponseDTO = CreateAttendeeResponseDTO.builder()
                .attendeeId(attendeeId)
                .externalUserId(externalUserId)
                .joinToken(joinToken)
                .build();


        AttendeeSession attendeeSession = AttendeeSession.builder()
                .attendeeId(attendeeId)
                .externalUserId(externalUserId)
                .joinToken(joinToken)
                .build();

        attendeeSessionService.save(attendeeSession);


        return createAttendeeResponseDTO;


    }


    public void deleteMeeting(String meetingId){

        DeleteMeetingRequest deleteMeetingRequest = DeleteMeetingRequest.builder()
                .meetingId(meetingId)
                .build();

        chimeSdkMeetingsClient.deleteMeeting(deleteMeetingRequest);

        meetingSessionService.deleteByMeetingId(meetingId);
    }


    public void deleteAttendee(String meetingId, String attendeeId){

        DeleteAttendeeRequest deleteAttendeeRequest = DeleteAttendeeRequest.builder()
                .meetingId(meetingId)
                .attendeeId(attendeeId)
                .build();

        chimeSdkMeetingsClient.deleteAttendee(deleteAttendeeRequest);


        attendeeSessionService.deleteByAttendeeId(attendeeId);

    }



}



































