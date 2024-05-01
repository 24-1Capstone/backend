package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.domain.dto.*;
import org.example.meeting.domain.MeetingSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;
import java.util.ArrayList;
import java.util.List;

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
                .meetingId(meetingID)
                .build();

        attendeeSessionService.save(attendeeSession);


        return createAttendeeResponseDTO;


    }


    //meetingSession 및 방에 참여중인 attendeeSession 삭제
    public void deleteMeeting(String meetingId){

        DeleteMeetingRequest deleteMeetingRequest = DeleteMeetingRequest.builder()
                .meetingId(meetingId)
                .build();

        chimeSdkMeetingsClient.deleteMeeting(deleteMeetingRequest);

        meetingSessionService.deleteByMeetingId(meetingId);

        attendeeSessionService.deleteByMeetingId(meetingId);


    }


    // attendeeSession 삭제
    public void deleteAttendee(String meetingId, String attendeeId){

        DeleteAttendeeRequest deleteAttendeeRequest = DeleteAttendeeRequest.builder()
                .meetingId(meetingId)
                .attendeeId(attendeeId)
                .build();

        chimeSdkMeetingsClient.deleteAttendee(deleteAttendeeRequest);


        attendeeSessionService.deleteByAttendeeId(attendeeId);

    }


    //해당 방에 있는 참여자 목록 반환
    public ListAttendeesResponseDTO listAttendeesResponseDTO(String meetingId){

        ListAttendeesRequest listAttendeesRequest = ListAttendeesRequest.builder()
                .meetingId(meetingId)
                .build();

        ListAttendeesResponse listAttendeesResponse = chimeSdkMeetingsClient.listAttendees(listAttendeesRequest);

        List<CreateAttendeeResponseDTO> attendeeDTOs = new ArrayList<>();
        for (Attendee attendee : listAttendeesResponse.attendees()) {
            attendeeDTOs.add(CreateAttendeeResponseDTO.builder()
                    .attendeeId(attendee.attendeeId())
                    .externalUserId(attendee.externalUserId())
                    .joinToken(attendee.joinToken())
                    .build());
        }

        return ListAttendeesResponseDTO.builder()
                .attendees(attendeeDTOs)
                .build();
    }


    public List<CreateMeetingResponseDTO> listMeetings(){
        List<MeetingSession> meetingSessions = meetingSessionService.findAll();
        List<CreateMeetingResponseDTO> responseDTOs = new ArrayList<>();

        for (MeetingSession meetingSession : meetingSessions) {
            CreateMeetingResponseDTO responseDTO = CreateMeetingResponseDTO.builder()
                    .externalMeetingId(meetingSession.getExternalMeetingId())
                    .mediaRegion(meetingSession.getMediaRegion())
                    .meetingArn(meetingSession.getMeetingArn())
                    .meetingId(meetingSession.getMeetingId())
                    .build();
            responseDTOs.add(responseDTO);
        }

        return responseDTOs;
    }


    public CreateMeetingWithAttendeesResponseDTO createMeetingWithAttendees(){

        // Chime SDK를 사용하여 회의 및 참가자 생성 요청
        CreateMeetingWithAttendeesRequest createMeetingWithAttendeesRequest = CreateMeetingWithAttendeesRequest.builder()
                .clientRequestToken("abc123")
                .externalMeetingId("meeting123")
                .mediaRegion("ap-northeast-2")
                .attendees(builder -> builder.externalUserId("user12314"))
                .build();

        CreateMeetingWithAttendeesResponse createMeetingWithAttendeesResponse = chimeSdkMeetingsClient.createMeetingWithAttendees(createMeetingWithAttendeesRequest);

        // 회의 및 참가자 정보 추출
        String externalMeetingId = createMeetingWithAttendeesResponse.meeting().externalMeetingId();
        String mediaRegion = createMeetingWithAttendeesResponse.meeting().mediaRegion();
        String meetingArn = createMeetingWithAttendeesResponse.meeting().meetingArn();
        String meetingId = createMeetingWithAttendeesResponse.meeting().meetingId();
        List<Attendee> attendeeList = createMeetingWithAttendeesResponse.attendees();

        // CreateAttendeeResponseDTO 리스트 생성
        List<CreateAttendeeResponseDTO> createAttendeeResponseDTOList = new ArrayList<>();
        for (Attendee attendee : attendeeList) {
            CreateAttendeeResponseDTO createAttendeeResponseDTO = CreateAttendeeResponseDTO.builder()
                    .attendeeId(attendee.attendeeId())
                    .externalUserId(attendee.externalUserId())
                    .joinToken(attendee.joinToken())
                    .build();
            createAttendeeResponseDTOList.add(createAttendeeResponseDTO);

            // AttendeeSession 객체 생성 및 저장
            AttendeeSession attendeeSession = AttendeeSession.builder()
                    .attendeeId(attendee.attendeeId())
                    .externalUserId(attendee.externalUserId())
                    .joinToken(attendee.joinToken())
                    .meetingId(meetingId)
                    .build();
            attendeeSessionService.save(attendeeSession);
        }

        // MeetingSession 객체 생성 및 저장
        MeetingSession meetingSession = MeetingSession.builder()
                .externalMeetingId(externalMeetingId)
                .mediaRegion(mediaRegion)
                .meetingArn(meetingArn)
                .meetingId(meetingId)
                .build();
        meetingSessionService.save(meetingSession);

        // CreateMeetingWithAttendeesResponseDTO 객체 생성하여 반환
        return CreateMeetingWithAttendeesResponseDTO.builder()
                .externalMeetingId(externalMeetingId)
                .mediaRegion(mediaRegion)
                .meetingArn(meetingArn)
                .meetingId(meetingId)
                .attendees(createAttendeeResponseDTOList)
                .build();
    }




}



































