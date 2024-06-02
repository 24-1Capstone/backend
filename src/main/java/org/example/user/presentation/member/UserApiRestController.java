package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.*;
import org.example.user.domain.entity.member.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Tag(name = "github-user-restapi", description = "회원 리스트를 보여주기 위한 RESTAPI")
@RequiredArgsConstructor
@RestController
public class UserApiRestController {

    private final WebClient webClient;
    private final UserService userService;

    @Operation(summary = "깃허브 사용자 정보 조회API", description = "깃허브 사용자 상세 정보 모두 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok!!"),
            @ApiResponse(responseCode = "304", description = "Not Modified"),
            @ApiResponse(responseCode = "401", description = "Requires authentication"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/api/user/userinfo")
    public Flux<GithubUserResponse> getUserInfo(HttpServletRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(userName);
        final String token = user.getAccessToken();

        return webClient.get()
                .uri("https://api.github.com/user")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(GithubUserResponse.class);
    }


    @Operation(summary = "요청 사용자 상세 정보 조회API", description = "요청 사용자 상세 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok!!"),
            @ApiResponse(responseCode = "404", description = "Resource not found!!")
    })
    @GetMapping("/api/users/{username}")
    public Flux<GithubUserResponse> getUserInfo(HttpServletRequest request, @PathVariable("username") String userName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        final String token = user.getAccessToken();
        String url = "https://api.github.com/users/" + userName;
        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(GithubUserResponse.class)
                .onErrorResume(e -> {
                    log.error("Failed to retrieve followers due to: {}", e.getMessage());
                    return Mono.error(new RuntimeException("API request failed with error "));
                });
    }




//    @Operation(summary = "깃허브 사용자 정보 조회API", description = "깃허브 사용자 상세 정보 모두 조회")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "ok!!"),
//            @ApiResponse(responseCode = "404", description = "user not found!!"),
//            @ApiResponse(responseCode = "500", description = "internal server error!!"),
//    })
////    @GetMapping("/api/userinfo")
//    public ResponseEntity findUserInfo() {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        System.out.println(userName);
//        GitHubProfile gitHubProfile = gitHubProfileService.findByUserName(userName);
//
//        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(gitHubProfile);
//
//        return ResponseEntity.ok()
//                .body(githubProfileResponse);
//    }





}










