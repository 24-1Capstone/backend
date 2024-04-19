package org.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;


@Configuration
public class ChimeConfig {

    private static final String ACCESS_KEY = "AKIA2UC3DREQRN2KSIXS";
    private static final String SECRET_KEY = "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";

    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);



    @Bean
    public ChimeSdkMeetingsClient chimeSdkMeetingsClient() {
        return ChimeSdkMeetingsClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

    }


}
