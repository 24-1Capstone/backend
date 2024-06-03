package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.AttendeeAlreadyExistsException;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.domain.dto.*;
import org.example.meeting.domain.MeetingSession;
import org.example.meeting.domain.dto.MediaPlacement;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ChimeService {

    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;
    private final MeetingSessionService meetingSessionService;
    private final AttendeeSessionService attendeeSessionService;


    // MeetingSession 엔티티에 저장 및 createMeetingResponseDTO 반환
    public CreateMeetingResponseDTO createMeeting(String applyUserName, String receiveUserName) {


        List<MeetingSession> applyUserMeetings = meetingSessionService.listMeetings(applyUserName);
        List<MeetingSession> receiveUserMeetings = meetingSessionService.listMeetings(receiveUserName);

        // 이미 회의가 생성되어 있는지 확인합니다.
        if (!applyUserMeetings.isEmpty() || !receiveUserMeetings.isEmpty()) {
            List<MeetingSession> existingMeetings = new ArrayList<>();
            existingMeetings.addAll(applyUserMeetings);
            existingMeetings.addAll(receiveUserMeetings);

            // 이미 있는 회의 중에서 applyUserName 또는 receiveUserName이 일치하는 경우를 찾습니다.
            Optional<MeetingSession> existingMeeting = existingMeetings.stream()
                    .filter(session -> session.getApplyUserName().equals(applyUserName) || session.getReceiveUserName().equals(receiveUserName))
                    .findFirst();

            if (existingMeeting.isPresent()) {
                // 이미 생성된 회의를 찾았으므로 해당 회의의 정보로 CreateMeetingResponseDTO를 생성하여 반환합니다.
                MeetingSession session = existingMeeting.get();
                return CreateMeetingResponseDTO.builder()
                        .externalMeetingId(session.getExternalMeetingId())
                        .mediaPlacement(MediaPlacement.builder()
                                .audioFallbackUrl(session.getAudioFallbackUrl())
                                .audioHostUrl(session.getAudioHostUrl())
                                .eventIngestionUrl(session.getEventIngestionUrl())
                                .screenDataUrl(session.getScreenDataUrl())
                                .screenSharingUrl(session.getScreenSharingUrl())
                                .screenViewingUrl(session.getScreenViewingUrl())
                                .signalingUrl(session.getSignalingUrl())
                                .turnControlUrl(session.getTurnControllerUrl())
                                .build())
                        .mediaRegion(session.getMediaRegion())
                        .meetingArn(session.getMeetingArn())
                        .meetingId(session.getMeetingId())
                        .applyUserName(session.getApplyUserName())
                        .receiveUserName(session.getReceiveUserName())
                        .build();
            }
        }

        CreateMeetingRequest request = CreateMeetingRequest.builder()
                .clientRequestToken(getRandomString())
                .externalMeetingId(getRandomString())
                .mediaRegion("ap-northeast-2")
                .build();

        CreateMeetingResponse createMeetingResponse = chimeSdkMeetingsClient.createMeeting(request);


        MeetingSession meetingSession = MeetingSession.builder()
                .externalMeetingId(createMeetingResponse.meeting().externalMeetingId())
                .mediaRegion(createMeetingResponse.meeting().mediaRegion())
                .meetingArn(createMeetingResponse.meeting().meetingArn())
                .meetingId(createMeetingResponse.meeting().meetingId())
                .audioFallbackUrl(createMeetingResponse.meeting().mediaPlacement().audioFallbackUrl())
                .audioHostUrl(createMeetingResponse.meeting().mediaPlacement().audioHostUrl())
                .eventIngestionUrl(createMeetingResponse.meeting().mediaPlacement().eventIngestionUrl())
                .screenDataUrl(createMeetingResponse.meeting().mediaPlacement().screenDataUrl())
                .screenSharingUrl(createMeetingResponse.meeting().mediaPlacement().screenSharingUrl())
                .screenViewingUrl(createMeetingResponse.meeting().mediaPlacement().screenViewingUrl())
                .signalingUrl(createMeetingResponse.meeting().mediaPlacement().signalingUrl())
                .turnControllerUrl(createMeetingResponse.meeting().mediaPlacement().turnControlUrl())
                .applyUserName(applyUserName)
                .receiveUserName(receiveUserName)
                .build();


        meetingSessionService.save(meetingSession);

        return CreateMeetingResponseDTO.builder()
                .externalMeetingId(meetingSession.getExternalMeetingId())
                .mediaPlacement(MediaPlacement.builder()
                        .audioFallbackUrl(meetingSession.getAudioFallbackUrl())
                        .audioHostUrl(meetingSession.getAudioHostUrl())
                        .eventIngestionUrl(meetingSession.getEventIngestionUrl())
                        .screenDataUrl(meetingSession.getScreenDataUrl())
                        .screenSharingUrl(meetingSession.getScreenSharingUrl())
                        .screenViewingUrl(meetingSession.getScreenViewingUrl())
                        .signalingUrl(meetingSession.getSignalingUrl())
                        .turnControlUrl(meetingSession.getTurnControllerUrl())
                        .build())
                .mediaRegion(meetingSession.getMediaRegion())
                .meetingArn(meetingSession.getMeetingArn())
                .meetingId(meetingSession.getMeetingId())
                .applyUserName(meetingSession.getApplyUserName())
                .receiveUserName(meetingSession.getReceiveUserName())
                .build();

    }



    // AttendeeSession 엔티티에 저장 및 createAttendeeResponseDTO 반환
    public CreateAttendeeResponseDTO createAttendee(String meetingID) {


        String externalUserId = SecurityContextHolder.getContext().getAuthentication().getName();



        // 이미 존재하는 참여자인지 확인
        Optional<AttendeeSession> existingAttendee = attendeeSessionService.findByExternalUserId(externalUserId);
        if (existingAttendee.isPresent()) {
            AttendeeSession session = existingAttendee.get();
            return CreateAttendeeResponseDTO.builder()
                    .attendeeId(session.getAttendeeId())
                    .externalUserId(session.getExternalUserId())
                    .joinToken(session.getJoinToken())
                    .build();
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
    public void deleteMeeting(String meetingId) {

        DeleteMeetingRequest deleteMeetingRequest = DeleteMeetingRequest.builder()
                .meetingId(meetingId)
                .build();

        chimeSdkMeetingsClient.deleteMeeting(deleteMeetingRequest);

        meetingSessionService.deleteByMeetingId(meetingId);

        attendeeSessionService.deleteByMeetingId(meetingId);


    }


    // 열려있는 내 모든 회의 조회

    public List<CreateMeetingResponseDTO> listMeetings() {
        List<MeetingSession> meetingSessions = meetingSessionService.listMeetings(SecurityContextHolder.getContext().getAuthentication().getName());
        List<CreateMeetingResponseDTO> responseDTOs = new ArrayList<>();

        for (MeetingSession meetingSession : meetingSessions) {
            MediaPlacement mediaPlacement = MediaPlacement.builder()
                    .audioFallbackUrl(meetingSession.getAudioFallbackUrl())
                    .audioHostUrl(meetingSession.getAudioHostUrl())
                    .eventIngestionUrl(meetingSession.getEventIngestionUrl())
                    .screenDataUrl(meetingSession.getScreenDataUrl())
                    .screenSharingUrl(meetingSession.getScreenSharingUrl())
                    .screenViewingUrl(meetingSession.getScreenViewingUrl())
                    .signalingUrl(meetingSession.getSignalingUrl())
                    .turnControlUrl(meetingSession.getTurnControllerUrl())
                    .build();

            CreateMeetingResponseDTO responseDTO = CreateMeetingResponseDTO.builder()
                    .externalMeetingId(meetingSession.getExternalMeetingId())
                    .mediaPlacement(mediaPlacement)
                    .mediaRegion(meetingSession.getMediaRegion())
                    .meetingArn(meetingSession.getMeetingArn())
                    .meetingId(meetingSession.getMeetingId())
                    .applyUserName(meetingSession.getApplyUserName())
                    .receiveUserName(meetingSession.getReceiveUserName())
                    .build();
            responseDTOs.add(responseDTO);
        }

        return responseDTOs;
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




