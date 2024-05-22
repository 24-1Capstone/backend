//package org.example.user.presentation.member;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.config.jwt.TokenProvider;
//import org.example.user.application.member.UserService;
//import org.example.user.domain.dto.request.token.GetAccessTokenRequest;
//import org.example.user.domain.dto.request.token.OAuthAccessTokenRequest;
//import org.example.user.domain.dto.response.token.OAuthTokensResponse;
//import org.example.user.domain.entity.member.User;
//import org.example.user.repository.member.UserRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class AuthController {
//
//    private final TokenProvider tokenProvider;
//    private final UserRepository userRepository;
//    private final UserService userService;
//
//    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
//    private static final RestTemplate restTemplate = new RestTemplate();
//
//    @Value("${spring.security.oauth2.client.registration.github.client-id}")
//    private String clientId;
//
//    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
//    private String clientSecret;
//
//    @GetMapping("/oauth2/callback")
//    public ResponseEntity<OAuthTokensResponse> getAccessToken(@RequestParam final String code) {
//        final OAuthTokensResponse response = getTokensInfo(code);
//        return ResponseEntity.ok().body(response);
//    }
//
//    public OAuthTokensResponse getTokensInfo(final String code) {
//        return restTemplate.postForObject(
//                ACCESS_TOKEN_URL,
//                new OAuthAccessTokenRequest(clientId, clientSecret, code),
//                OAuthTokensResponse.class
//        );
//    }
//
//    @PutMapping("/api/access")
//    public ResponseEntity<String> getAccessToken(@RequestBody GetAccessTokenRequest accessTokenRequest) {
//        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        User user = userService.findByUsername(userName);
//        if (user != null) {
//            userService.update(userName, accessTokenRequest.getAccessToken());
//        } else {
//            // 사용자가 없으면 새로운 사용자를 생성하거나 에러를 반환
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        return ResponseEntity.ok("Access Token updated successfully");
//    }
//
//
//}