package org.example.user.presentation.member;

import lombok.RequiredArgsConstructor;
import org.example.user.application.member.UserService;
import org.example.user.domain.dto.response.member.FollowerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiRestController {

    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<FollowerResponse>> findAllUsers() {
        List<FollowerResponse> users = userService.findAll()
                .stream()
                .map(u -> new FollowerResponse(u))
                .toList();

        return ResponseEntity.ok()
                .body(users);
    }

}
