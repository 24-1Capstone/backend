package org.example.user.presentation.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.user.application.member.GitHubProfileService;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.example.user.domain.dto.response.member.GithubProfileResponse;
import org.example.user.domain.entity.member.GitHubProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiRestController {

    private final UserService userService;
    private final GitHubProfileService gitHubProfileService;


    @GetMapping("/api/users")
    public ResponseEntity<List<FollowerResponse>> findAllUsers() {
        List<FollowerResponse> users = userService.findAll()
                .stream()
                .map(u -> new FollowerResponse(u))
                .toList();

        return ResponseEntity.ok()
                .body(users);
    }


    @GetMapping("/api/userinfo")
    public ResponseEntity findUserInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        GitHubProfile gitHubProfile = gitHubProfileService.findByUserName(userName);

        GithubProfileResponse githubProfileResponse = new GithubProfileResponse(gitHubProfile);

        return ResponseEntity.ok()
                .body(githubProfileResponse);
    }



}










