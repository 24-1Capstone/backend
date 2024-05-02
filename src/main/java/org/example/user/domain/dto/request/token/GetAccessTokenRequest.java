package org.example.user.domain.dto.request.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAccessTokenRequest {
    @JsonProperty("access_token")
    private String accessToken;
}
