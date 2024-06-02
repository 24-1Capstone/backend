package org.example.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("aws") // 자바 클래스에 프로피티값을 가져와서 사용하는 애너테이션
public class AwsProperties {
    private String access_key;
    private String secret_key;

}
