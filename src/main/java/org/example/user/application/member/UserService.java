package org.example.user.application.member;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.UserNotFoundException;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.FollowingResponse;
import org.example.user.domain.dto.response.member.GithubProfileResponse;
import org.example.user.domain.entity.member.User;
import org.example.user.domain.dto.request.member.AddUserRequest;
import org.example.user.repository.member.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    public UserService(UserRepository userRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.webClient = webClient;
    }

    public Flux<GithubProfileResponse> fetchUserInfo(String accessToken) {
        return webClient.get()
                .uri("/user")
                .header("Authorization", "token " + accessToken)
                .header("User-Agent", "YourApp")
                .retrieve()
                .bodyToFlux(GithubProfileResponse.class);
    }


    public Flux<FollowerResponse> fetchFollowers(String followersUrl) {
        return webClient.get()
                .uri(followersUrl)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    // 4xx 오류 로깅
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("API request failed with 4xx error: " + body);
                        return Mono.error(new RuntimeException("API request failed with error: " + body));
                    });
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    // 5xx 오류 처리
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Server error on API request: " + body);
                        return Mono.error(new RuntimeException("Server error on API request"));
                    });
                })
                .bodyToFlux(FollowerResponse.class);
    }

    public Flux<FollowingResponse> fetchFollowings(User user, int pageSize, int page) {
        String processedUrl = user.getFollowingsUrl().replace("{/other_user}", "");

        return webClient.get()
                .uri(processedUrl +  "?per_page=" + pageSize + "&page=" + page)
                .header("Authorization", "Bearer " + user.getAccessToken())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    // 4xx 오류 로깅
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("API request failed with 4xx error: " + body);
                        return Mono.error(new RuntimeException("API request failed with error: " + body));
                    });
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    // 5xx 오류 처리
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("Server error on API request: " + body);
                        return Mono.error(new RuntimeException("Server error on API request"));
                    });
                })
                .bodyToFlux(FollowingResponse.class);
    }

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .avatarUrl(dto.getAvatarUrl())
                .followersUrl(dto.getFollowersUrl())
                .followingsUrl(dto.getFollowingsUrl())
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Unexpected user"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("unexpected user"));
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void update(String username, String accessToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("unexpected user"));
        user.setAccessToken(accessToken);

    }


}
//    // 이 메서드는 새 사용자를 생성하고 저장합니다. 실제 사용 시 비밀번호 등 추가 정보 처리가 필요할 수 있습니다.
//    private User createUser(String username) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        User newUser = User.builder()
//                .username(username)
//                // 임시 비밀번호 설정; 실제 상황에서는 보다 안전한 접근이 필요합니다.
//                .password(encoder.encode("defaultPassword"))
//                .avatarUrl("image")
//                .build();
//        return userRepository.save(newUser);
//    }
