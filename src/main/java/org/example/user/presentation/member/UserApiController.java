package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.domain.dto.request.member.AddUserRequest;
import org.example.user.application.member.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "user-api-controller", description = "회원가입, 로그아웃을 위한 컨트롤러")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

//    @PostMapping("/user")
//    public String signup(AddUserRequest request) {
//        userService.save(request); // 회원가입 메서드 호출
//        return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
//    }

    @Operation(summary = "사용자 로그아웃", description = "로그아웃 하면 refresh_token 쿠키에서 제거, authentication 제거")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ok!!"),
            @ApiResponse(responseCode = "401", description = "unauthorized user!!"),
            @ApiResponse(responseCode = "500", description = "internal server error!!"),
    })
    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity logout() {
        return ResponseEntity.ok("You have been logged out successfully!!.");
    }


}
