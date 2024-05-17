package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.jwt.TokenProvider;
import org.example.exception.UserNotFoundException;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.FollowingResponse;
import org.example.user.domain.entity.member.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/api/user/following")
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
            return userService.fetchFollowings(user, pageSize, page);
        } else {
            return null;
        }
    }

//    @Operation(summary = "깃허브 비회원 Following 정보 조회API", description = "회원가입하지 않은 깃허브 사용자 Following 정보 조회")
//    @GetMapping("/api/user/non-registered-following")
//    public Flux<FollowingResponse> getNonRegisteredFollowings(Authentication authentication,
//                                                              @RequestParam(value = "per_page", defaultValue = "100") int pageSize,
//                                                              @RequestParam(value = "page", defaultValue = "1") int page) {
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            String token = (String) authentication.getCredentials();
//            log.info("token:{}", token);
//            Long userId = tokenProvider.getUserId(token);
//            User user = userService.findById(userId);
//            log.info("userId:{}", userId);
//            log.info("user:{}", user);
//            return userService.fetchNonRegisteredFollowings(user, pageSize, page);
//        } else {
//            return Flux.empty();
//        }
//    }


    @Operation(summary = "깃허브 사용자 Follower 정보 조회API", description = "깃허브 사용자 Follower 정보 모두 조회")
    @GetMapping("/api/user/followers")
    public Flux<FollowerResponse> getFollower(HttpServletRequest request,
                                              @RequestParam(value = "per_page", defaultValue = "100") int pageSize,
                                              @RequestParam(value = "page", defaultValue = "1") int page) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(userName);
        final String token = user.getAccessToken();
        String url = "https://api.github.com/users/" + userName + "/followers";
        return webClient.get()
                .uri(url + "?per_page=" + pageSize + "&page=" + page)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(FollowerResponse.class)
                .flatMap(followerResponse -> Mono.just(followerResponse)
                        .flatMap(response -> {
                            try {
                                userService.findByUsername(response.getLogin());
                                return Mono.just(response);
                            } catch (UserNotFoundException e) {
                                return Mono.empty(); // 데이터베이스에 없는 사용자는 무시
                            }
                        }))
                .onErrorResume(e -> {
                    log.error("Failed to retrieve followers due to: {}", e.getMessage());
                    return Flux.error(new RuntimeException("API request failed with error "));  // API 오류 메시지 반환
                });
    }

//    @Operation(summary = "깃허브 사용자 비회원 Follower 정보 조회API", description = "회원가입하지 않은 깃허브 사용자 Follower 정보 조회")
//    @GetMapping("/api/user/non-registered-followers")
//    public Flux<FollowerResponse> getNonRegisteredFollowers(HttpServletRequest request,
//                                                            @RequestParam(value = "per_page", defaultValue = "100") int pageSize,
//                                                            @RequestParam(value = "page", defaultValue = "1") int page) {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userService.findByUsername(userName);
//        final String token = user.getAccessToken();
//        String url = "https://api.github.com/users/" + userName + "/followers";
//        return webClient.get()
//                .uri(url + "?per_page=" + pageSize + "&page=" + page)
//                .header("Authorization", "Bearer " + token)
//                .retrieve()
//                .bodyToFlux(FollowerResponse.class)
//                .flatMap(followerResponse -> Mono.just(followerResponse)
//                        .flatMap(response -> {
//                            try {
//                                userService.findByUsername(response.getLogin());
//                                return Mono.empty(); // 회원가입한 사용자는 무시
//                            } catch (UserNotFoundException e) {
//                                return Mono.just(response); // 회원가입하지 않은 사용자만 반환
//                            }
//                        }))
//                .onErrorResume(e -> {
//                    log.error("Failed to retrieve followers due to: {}", e.getMessage());
//                    return Flux.error(new RuntimeException("API request failed with error"));  // API 오류 메시지 반환
//                });
//    }


    @PutMapping("/api/user/following/{username}")
    public Flux<String> followUser(HttpServletRequest request, @PathVariable String username) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(userName);

        final String token = user.getAccessToken();
        String url = "https://api.github.com/user/following/";
        return webClient.put()
                .uri(url + username)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(e -> {
                    return Mono.just("Failed to follow user: " + e.getMessage());
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
}
