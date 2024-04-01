package org.example.domain.dto.request.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String username;
    private String password;
    private String avatarUrl;

}
