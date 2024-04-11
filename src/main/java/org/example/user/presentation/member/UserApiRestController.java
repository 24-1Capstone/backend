package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.user.application.member.GitHubProfileService;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.GithubProfileResponse;
import org.example.user.domain.entity.member.GitHubProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "user-restapi-controller", description = "회원 리스트를 보여주기 위한 RESTAPI")
@RequiredArgsConstructor
@RestController
public class UserApiRestController {

    private final UserService userService;
    private final GitHubProfileService gitHubProfileService;

    @Operation(summary = "사용자 정보 조회API", description = "모든 사용자 상세 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok!!"),
            @ApiResponse(responseCode = "404", description = "user not found!!"),
            @ApiResponse(responseCode = "500", description = "internal server error!!"),
    })
    @GetMapping("/api/users")
    public ResponseEntity<List<FollowerResponse>> findAllUsers() {
        List<FollowerResponse> users = userService.findAll()
                .stream()
                .map(u -> new FollowerResponse(u))
                .toList();

        return ResponseEntity.ok()
                .body(users);
    }


    @GetMapping("/api/users/userinfo")
    public ResponseEntity findUserInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        GitHubProfile gitHubProfile = gitHubProfileService.findByUserName(userName);

        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(gitHubProfile);

        return ResponseEntity.ok()
                .body(githubProfileResponse);
    }



}










