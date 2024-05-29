package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.exception.AttendeeAlreadyExistsException;
import org.example.exception.MeetingSessionNotFoundException;
import org.example.meeting.domain.AttendeeSession;
import org.example.meeting.domain.dto.*;
import org.example.meeting.domain.MeetingSession;
import org.example.meeting.domain.dto.MediaPlacement;
import org.example.user.application.member.UserService;
import org.example.user.domain.entity.member.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
@Transactional
public class ChimeService {

    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;
    private final MeetingSessionService meetingSessionService;
    private final AttendeeSessionService attendeeSessionService;
    private final UserService userService;
    private final ThreadPoolTaskScheduler taskScheduler;

    private final ConcurrentMap<String, Long> meetingExpiryMap = new ConcurrentHashMap<>();


    // MeetingSession 엔티티에 저장 및 createMeetingResponseDTO 반환
    public void createMeeting(String applyUserName, String receiveUserName) {


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

        // 초기 만료 시간 설정
        meetingExpiryMap.put(meetingSession.getMeetingId(), System.currentTimeMillis() + 10 * 60 * 1000);

        // 첫 번째 쿠폰 체크 작업 예약
        taskScheduler.schedule(() -> checkMeetingExpiry(meetingSession.getMeetingId(), applyUserName), new Date(System.currentTimeMillis() + 10 * 60 * 1000));


    }



    // AttendeeSession 엔티티에 저장 및 createAttendeeResponseDTO 반환
    public CreateAttendeeResponseDTO createAttendee(String meetingID) {


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

    // 주기적인 쿠폰 체크 메서드
    @Scheduled(fixedRate = 10 * 60 * 1000) // 10분마다 실행
    public void checkMeetingExpiry() {
        long currentTime = System.currentTimeMillis();
        meetingExpiryMap.forEach((meetingId, expiryTime) -> {
            if (currentTime >= expiryTime) {
                MeetingSession meetingSession = meetingSessionService.findByMeetingId(meetingId)
                        .orElseThrow(() -> new MeetingSessionNotFoundException("meeting not find"));
                if (meetingSession != null) {
                    checkMeetingExpiry(meetingId, meetingSession.getApplyUserName());
                }
            }
        });
    }

    // 개별 회의 만료 체크 메서드
    public void checkMeetingExpiry(String meetingId, String applyUserName) {
        long currentTime = System.currentTimeMillis();
        Long expiryTime = meetingExpiryMap.get(meetingId);
        if (expiryTime != null && currentTime >= expiryTime) {
            User user = userService.findByUsername(applyUserName);
            if (user != null && user.getCouponCount() > 0) {
                // 회의를 연장하고 쿠폰 수를 감소
                user.useCoupon();
                userService.saveUser(user);
                meetingExpiryMap.put(meetingId, currentTime + 10 * 60 * 1000);
                // 다음 체크 작업 예약
                taskScheduler.schedule(() -> checkMeetingExpiry(meetingId, applyUserName), new Date(currentTime + 10 * 60 * 1000));
            } else {
                // 쿠폰이 없으면 회의를 삭제
                deleteMeeting(meetingId);
            }
        }
    }

}





