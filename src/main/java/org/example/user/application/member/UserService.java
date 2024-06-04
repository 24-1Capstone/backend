package org.example.user.application.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.UserNotFoundException;
import org.example.meeting.application.AttendeeSessionService;
import org.example.meeting.application.MeetingSessionService;
import org.example.meeting.domain.MeetingSession;
import org.example.reservation.application.ReservationService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.FollowingResponse;
import org.example.user.domain.dto.response.member.GithubProfileResponse;
import org.example.user.domain.entity.member.User;
import org.example.user.domain.dto.request.member.AddUserRequest;
import org.example.user.repository.member.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;
    private final MeetingSessionService meetingSessionService;
    private final AttendeeSessionService attendeeSessionService;
    private final ReservationService reservationService;




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
                .bodyToFlux(FollowingResponse.class)
                .flatMap(followingResponse -> Mono.just(followingResponse)
                        .flatMap(response -> {
                            try {
                                findByUsername(response.getLogin());
                                return Mono.just(response);
                            } catch (UserNotFoundException e) {
                                return Mono.empty(); // 데이터베이스에 없는 사용자는 무시
                            }
                        }))
                .onErrorResume(e -> {
                    log.error("Failed to retrieve followings due to: {}", e.getMessage());
                    return Flux.error(new RuntimeException("API request failed with error "));  // API 오류 메시지 반환
                });
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

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
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


    public Flux<FollowingResponse> fetchNonRegisteredFollowings(User user, int pageSize, int page) {
        String processedUrl = user.getFollowingsUrl().replace("{/other_user}", "");

        return webClient.get()
                .uri(processedUrl + "?per_page=" + pageSize + "&page=" + page)
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
                .bodyToFlux(FollowingResponse.class)
                .flatMap(followingResponse -> Mono.just(followingResponse)
                        .flatMap(response -> {
                            try {
                                findByUsername(response.getLogin());
                                return Mono.empty(); // 회원가입한 사용자는 무시
                            } catch (UserNotFoundException e) {
                                return Mono.just(response); // 회원가입하지 않은 사용자만 반환
                            }
                        }))
                .onErrorResume(e -> {
                    log.error("Failed to retrieve followings due to: {}", e.getMessage());
                    return Flux.error(new RuntimeException("API request failed with error"));  // API 오류 메시지 반환
                });
    }

    public void deleteUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        reservationService.deleteReservationByUserName(userName);
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + userName));
        userRepository.deleteById(user.getId());

        List<MeetingSession> sessionList = meetingSessionService.listMeetings(userName);
        for (MeetingSession session : sessionList) {
            meetingSessionService.deleteByMeetingSessionId(session.getId());
        }

        attendeeSessionService.deleteByExternalUserId(userName);
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

    public void saveUser(User user){

        userRepository.save(user);

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
