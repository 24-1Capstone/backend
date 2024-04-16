package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.jwt.TokenProvider;
import org.example.user.application.member.GitHubProfileService;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.AllUsersResponse;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.FollowingResponse;
import org.example.user.domain.dto.response.member.GithubProfileResponse;
import org.example.user.domain.entity.member.GitHubProfile;
import org.example.user.domain.entity.member.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Tag(name = "user-restapi-controller", description = "회원 리스트를 보여주기 위한 RESTAPI")
@RequiredArgsConstructor
@RestController
public class UserApiRestController {

    private final UserService userService;
    private final GitHubProfileService gitHubProfileService;
    private final WebClient webClient;
    private final TokenProvider tokenProvider;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Operation(summary = "깃허브 사용자 정보 조회API", description = "깃허브 사용자 상세 정보 모두 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok!!"),
            @ApiResponse(responseCode = "404", description = "user not found!!"),
            @ApiResponse(responseCode = "500", description = "internal server error!!"),
    })
    @GetMapping("/api/user/userinfo")
    public ResponseEntity findUserInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(userName);
        GitHubProfile gitHubProfile = gitHubProfileService.findByUserName(userName);

        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(gitHubProfile);

        return ResponseEntity.ok()
                .body(githubProfileResponse);
    }

    @Operation(summary = "깃허브 사용자 Following 정보 조회API", description = "깃허브 사용자 Following 정보 모두 조회")
    @GetMapping("/user/following")
    public Flux<FollowingResponse> getFollowings(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getCredentials();
            log.info("token:{}",token);
            Long userId = tokenProvider.getUserId(token);
            User user = userService.findById(userId);
            log.info("userId:{}", userId);
            log.info("user:{}",user);
            return userService.fetchFollowings(user.getFollowingsUrl());
        } else {
            return null;
        }
    }

    @Operation(summary = "깃허브 사용자 Follower 정보 조회API", description = "깃허브 사용자 Follower 정보 모두 조회")
    @GetMapping("/user/followers")
    public Flux<FollowerResponse> getFollower(HttpServletRequest request) {
        final String token = extractToken(request);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String url = "https://api.github.com/users/" + userName + "/followers";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(FollowerResponse.class)
                .onErrorResume(e -> {
                    log.error("Failed to retrieve followers due to: {}", e.getMessage());
                    return Mono.error(new RuntimeException("API request failed with error "));  // API 오류 메시지 반환
                });
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 다음부터의 문자열을 추출
        } else {
            throw new RuntimeException("No token provided");
        }
    }

//    @Operation(summary = "모든 사용자 정보 조회API", description = "모든 사용자 상세 정보를 조회")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "ok!!"),
//            @ApiResponse(responseCode = "404", description = "user not found!!"),
//            @ApiResponse(responseCode = "500", description = "internal server error!!"),
//    })
//    @GetMapping("/api/user")
//    public ResponseEntity<List<AllUsersResponse>> findAllUsers() {
//        List<AllUsersResponse> users = userService.findAll()
//                .stream()
//                .map(u -> new AllUsersResponse(u))
//                .toList();
//
//        return ResponseEntity.ok()
//                .body(users);
//    }

//    @Operation(summary = "깃허브 사용자 정보 조회API", description = "깃허브 사용자 Follower 정보 모두 조회")
//    @GetMapping("/api/user")
//    public Flux<String> getUserInfo(HttpServletRequest request) {
//        final String token = extractToken(request);
//        return webClient.get()
//                .uri("https://api.github.com/users")
//                .header("Authorization", "Bearer " + token)
//                .header("User-Agent", "spring-developer")
//                .retrieve()
//                .bodyToFlux(String.class);
//    }
}










