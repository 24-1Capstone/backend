package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.AttendeeAlreadyExistsException;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.domain.dto.*;
import org.example.meeting.domain.MeetingSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
                .clientRequestToken(getRandomString())
                .externalMeetingId(getRandomString())
                .mediaRegion("ap-northeast-2")
                .build();

        CreateMeetingResponse createMeetingResponse = chimeSdkMeetingsClient.createMeeting(request);

        String externalMeetingId = createMeetingResponse.meeting().externalMeetingId();
        String mediaRegion = createMeetingResponse.meeting().mediaRegion();
        String metingArn = createMeetingResponse.meeting().meetingArn();
        String meetingId = createMeetingResponse.meeting().meetingId();
        String audioFallbackUrl = createMeetingResponse.meeting().mediaPlacement().audioFallbackUrl();
        String audioHostUrl = createMeetingResponse.meeting().mediaPlacement().audioHostUrl();
        String eventIngestionUrl = createMeetingResponse.meeting().mediaPlacement().eventIngestionUrl();
        String screenDataUrl = createMeetingResponse.meeting().mediaPlacement().screenDataUrl();
        String screenSharingUrl = createMeetingResponse.meeting().mediaPlacement().screenSharingUrl();
        String screenViewingUrl = createMeetingResponse.meeting().mediaPlacement().screenViewingUrl();
        String signalingUrl = createMeetingResponse.meeting().mediaPlacement().signalingUrl();
        String turnControlUrl = createMeetingResponse.meeting().mediaPlacement().turnControlUrl();


        CreateMeetingResponseDTO createMeetingResponseDTO = CreateMeetingResponseDTO.builder()
                .externalMeetingId(externalMeetingId)
                .mediaRegion(mediaRegion)
                .meetingArn(metingArn)
                .meetingId(meetingId)
                .audioFallbackUrl(audioFallbackUrl)
                .audioHostUrl(audioHostUrl)
                .eventIngestionUrl(eventIngestionUrl)
                .screenDataUrl(screenDataUrl)
                .screenSharingUrl(screenSharingUrl)
                .screenViewingUrl(screenViewingUrl)
                .signalingUrl(signalingUrl)
                .turnControllerUrl(turnControlUrl)
                .build();

        MeetingSession meetingSession = MeetingSession.builder()
                .externalMeetingId(externalMeetingId)
                .mediaRegion(mediaRegion)
                .meetingArn(metingArn)
                .meetingId(meetingId)
                .audioFallbackUrl(audioFallbackUrl)
                .audioHostUrl(audioHostUrl)
                .eventIngestionUrl(eventIngestionUrl)
                .screenDataUrl(screenDataUrl)
                .screenSharingUrl(screenSharingUrl)
                .screenViewingUrl(screenViewingUrl)
                .signalingUrl(signalingUrl)
                .turnControllerUrl(turnControlUrl)
                .build();


        meetingSessionService.save(meetingSession);




        return createMeetingResponseDTO;

    }


    // AttendeeSession 엔티티에 저장 및 createAttendeeResponseDTO 반환
    public CreateAttendeeResponseDTO createAttendeeResponseDTO(String meetingID){


        String externalUserId = SecurityContextHolder.getContext().getAuthentication().getName();


        // 이미 존재하는 참여자인지 확인
        Optional<AttendeeSession> existingAttendee = attendeeSessionService.findByExternalUserId(externalUserId);
        if (existingAttendee.isPresent()) {
            throw new AttendeeAlreadyExistsException("You are already an attendee of this meeting.");
        }


        //externalUserId 내 아이디로 설정
        CreateAttendeeRequest request = CreateAttendeeRequest.builder()
                .meetingId(meetingID)
                .externalUserId(externalUserId)
                .build();



        CreateAttendeeResponse createAttendeeResponse = chimeSdkMeetingsClient.createAttendee(request);


        String attendeeId = createAttendeeResponse.attendee().attendeeId();
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


    // 열려있는 모든 회의 조회

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



    // 회의 생성 동시에 참가자 생성
    public CreateMeetingWithAttendeesResponseDTO createMeetingWithAttendees(){



        // Chime SDK 를 사용하여 회의 및 참가자 생성 요청
        CreateMeetingWithAttendeesRequest createMeetingWithAttendeesRequest = CreateMeetingWithAttendeesRequest.builder()
                .clientRequestToken(getRandomString())
                .externalMeetingId(getRandomString())
                .mediaRegion("ap-northeast-2")
                .attendees(builder -> builder.externalUserId(SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();

        CreateMeetingWithAttendeesResponse createMeetingWithAttendeesResponse = chimeSdkMeetingsClient.createMeetingWithAttendees(createMeetingWithAttendeesRequest);

        // 회의 및 참가자 정보 추출
        String externalMeetingId = createMeetingWithAttendeesResponse.meeting().externalMeetingId();
        String mediaRegion = createMeetingWithAttendeesResponse.meeting().mediaRegion();
        String metingArn = createMeetingWithAttendeesResponse.meeting().meetingArn();
        String meetingId = createMeetingWithAttendeesResponse.meeting().meetingId();
        String audioFallbackUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().audioFallbackUrl();
        String audioHostUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().audioHostUrl();
        String eventIngestionUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().eventIngestionUrl();
        String screenDataUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().screenDataUrl();
        String screenSharingUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().screenSharingUrl();
        String screenViewingUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().screenViewingUrl();
        String signalingUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().signalingUrl();
        String turnControlUrl = createMeetingWithAttendeesResponse.meeting().mediaPlacement().turnControlUrl();
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
                .meetingArn(metingArn)
                .meetingId(meetingId)
                .audioFallbackUrl(audioFallbackUrl)
                .audioHostUrl(audioHostUrl)
                .eventIngestionUrl(eventIngestionUrl)
                .screenDataUrl(screenDataUrl)
                .screenSharingUrl(screenSharingUrl)
                .screenViewingUrl(screenViewingUrl)
                .signalingUrl(signalingUrl)
                .turnControllerUrl(turnControlUrl)
                .build();
        meetingSessionService.save(meetingSession);

        // CreateMeetingWithAttendeesResponseDTO 객체 생성하여 반환
        return CreateMeetingWithAttendeesResponseDTO.builder()
                .externalMeetingId(externalMeetingId)
                .mediaRegion(mediaRegion)
                .meetingArn(metingArn)
                .meetingId(meetingId)
                .audioFallbackUrl(audioFallbackUrl)
                .audioHostUrl(audioHostUrl)
                .eventIngestionUrl(eventIngestionUrl)
                .screenDataUrl(screenDataUrl)
                .screenSharingUrl(screenSharingUrl)
                .screenViewingUrl(screenViewingUrl)
                .signalingUrl(signalingUrl)
                .turnControllerUrl(turnControlUrl)
                .attendees(createAttendeeResponseDTOList)
                .build();
    }



    public static String getRandomString() {
        return getRandomString(2, 64);
    }

    public static String getRandomString(int minLength, int maxLength) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }




}