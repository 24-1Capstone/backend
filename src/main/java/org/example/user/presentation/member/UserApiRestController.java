package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.application.member.GitHubProfileService;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.GithubProfileResponse;
import org.example.user.domain.entity.member.GitHubProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Tag(name = "user-restapi-controller", description = "회원 리스트를 보여주기 위한 RESTAPI")
@RequiredArgsConstructor
@RestController
public class UserApiRestController {

    private final UserService userService;
    private final GitHubProfileService gitHubProfileService;
    private final WebClient webClient;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Operation(summary = "모든 사용자 정보 조회API", description = "모든 사용자 상세 정보를 조회")
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

    @Operation(summary = "깃허브 사용자 정보 조회API", description = "깃허브 사용자 상세 정보 모두 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok!!"),
            @ApiResponse(responseCode = "404", description = "user not found!!"),
            @ApiResponse(responseCode = "500", description = "internal server error!!"),
    })
    @GetMapping("/api/users/userinfo")
    public ResponseEntity findUserInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(userName);
        GitHubProfile gitHubProfile = gitHubProfileService.findByUserName(userName);

        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(gitHubProfile);

        return ResponseEntity.ok()
                .body(githubProfileResponse);
    }
}










