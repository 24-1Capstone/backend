package org.example.meeting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;
import software.amazon.awssdk.services.chimesdkmeetings.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChimeService {

    private final ChimeSdkMeetingsClient chimeSdkMeetingsClient;

    public CreateMeetingResponse createMeetingResponse(CreateMeetingRequest request) {
        CreateMeetingRequest.Builder builder = CreateMeetingRequest.builder()
                .clientRequestToken(request.getValueForField("ClientRequestToken", String.class).orElse(null))
                .externalMeetingId(request.getValueForField("ExternalMeetingId", String.class).orElse(null))
                .mediaRegion(request.getValueForField("MediaRegion", String.class).orElse(null))
                .meetingHostId(request.getValueForField("MeetingHostId", String.class).orElse(null))
                .primaryMeetingId(request.getValueForField("PrimaryMeetingId", String.class).orElse(null));


        // AttendeeConfiguration 구성 요소
        AttendeeFeatures attendeeFeatures = AttendeeFeatures.builder()
                .maxCount(request.getValueForField("MeetingFeatures.Attendee.MaxCount", Integer.class).orElse(null))
                .build();

// AudioConfiguration 구성 요소
        AudioFeatures audioFeatures = AudioFeatures.builder()
                .echoReduction(request.getValueForField("MeetingFeatures.Audio.EchoReduction", String.class).orElse(null))
                .build();

// ContentConfiguration 구성 요소
        ContentFeatures contentFeatures = ContentFeatures.builder()
                .maxResolution(request.getValueForField("MeetingFeatures.Content.MaxResolution", String.class).orElse(null))
                .build();

// VideoConfiguration 구성 요소
        VideoFeatures videoFeatures = VideoFeatures.builder()
                .maxResolution(request.getValueForField("MeetingFeatures.Video.MaxResolution", String.class).orElse(null))
                .build();


        // MeetingFeaturesConfiguration 구성 요소
        MeetingFeaturesConfiguration meetingFeaturesConfiguration = MeetingFeaturesConfiguration.builder()
                .attendee(attendeeFeatures)
                .audio(audioFeatures)
                .content(contentFeatures)
                .video(videoFeatures)
                .build();


// NotificationsConfiguration 설정
        NotificationsConfiguration notificationsConfiguration = NotificationsConfiguration.builder()
                .lambdaFunctionArn(request.getValueForField("NotificationsConfiguration.LambdaFunctionArn", String.class).orElse(null))
                .snsTopicArn(request.getValueForField("NotificationsConfiguration.SnsTopicArn", String.class).orElse(null))
                .sqsQueueArn(request.getValueForField("NotificationsConfiguration.SqsQueueArn", String.class).orElse(null))
                .build();
        builder.notificationsConfiguration(notificationsConfiguration);

// Tags 설정
        List<Tag> tags = new ArrayList<>();
        List<Map<String, String>> tagsList = request.getValueForField("Tags", List.class).orElse(Collections.emptyList());
        for (Map<String, String> tagMap : tagsList) {
            String key = tagMap.get("Key");
            String value = tagMap.get("Value");
            Tag tag = Tag.builder()
                    .key(key)
                    .value(value)
                    .build();
            tags.add(tag);
        }

        builder.tags(tags);

// TenantIds 설정
        List<String> tenantIds = request.getValueForField("TenantIds", List.class).orElse(Collections.emptyList());
        builder.tenantIds(tenantIds);

        CreateMeetingRequest createMeetingRequest = builder.build();

        return chimeSdkMeetingsClient.createMeeting(createMeetingRequest);
    }
}

