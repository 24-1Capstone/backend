package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.jwt.TokenProvider;
import org.example.user.application.member.GitHubProfileService;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.FollowingResponse;
import org.example.user.domain.entity.member.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Tag(name = "github-follower-restapi", description = "GITHUB 팔로우, 팔로워 목록을 보여주기 위한 RESTAPI")
@RequiredArgsConstructor
@RestController
public class GithubFollowerApiRestController {
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final WebClient webClient;

    @Operation(summary = "깃허브 사용자 Following 정보 조회API", description = "깃허브 사용자 Following 정보 모두 조회")
    @GetMapping("/user/following")
    public Flux<FollowingResponse> getFollowings(Authentication authentication,
                                                 @RequestParam(value = "per_page", defaultValue = "100") int pageSize,
                                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getCredentials();
            log.info("token:{}",token);
            Long userId = tokenProvider.getUserId(token);
            User user = userService.findById(userId);
            log.info("userId:{}", userId);
            log.info("user:{}",user);
            return userService.fetchFollowings(user.getFollowingsUrl(), pageSize, page);
        } else {
            return null;
        }
    }

    @Operation(summary = "깃허브 사용자 Follower 정보 조회API", description = "깃허브 사용자 Follower 정보 모두 조회")
    @GetMapping("/user/followers")
    public Flux<FollowerResponse> getFollower(HttpServletRequest request,
                                              @RequestParam(value = "per_page", defaultValue = "100") int pageSize,
                                              @RequestParam(value = "page", defaultValue = "1") int page) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String url = "https://api.github.com/users/" + userName + "/followers";
        return webClient.get()
                .uri(url + "?per_page=" + pageSize + "&page=" + page)
                .retrieve()
                .bodyToFlux(FollowerResponse.class)
                .onErrorResume(e -> {
                    log.error("Failed to retrieve followers due to: {}", e.getMessage());
                    return Mono.error(new RuntimeException("API request failed with error "));  // API 오류 메시지 반환
                });
    }
}
