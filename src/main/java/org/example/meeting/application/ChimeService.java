package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.example.meeting.domain.CreateMeetingResponseDTO;
import org.example.meeting.domain.MeetingSession;
import org.example.meeting.repository.MeetingSessionRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateMeetingRequest;
import software.amazon.awssdk.services.chimesdkmeetings.model.CreateMeetingResponse;

@Service
@RequiredArgsConstructor
public class ChimeService {

    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;
    private final MeetingSessionService meetingSessionService;



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







        return createMeetingResponseDTO;

}

}

