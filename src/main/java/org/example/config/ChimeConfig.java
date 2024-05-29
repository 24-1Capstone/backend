package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chimesdkmeetings.ChimeSdkMeetingsClient;

@Configuration
public class ChimeConfig {

    private final AwsProperties awsProperties;

    public ChimeConfig(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @Bean
    public ChimeSdkMeetingsClient chimeSdkMeetingsClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsProperties.getAccess_key(), awsProperties.getSecret_key());

        return ChimeSdkMeetingsClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}
