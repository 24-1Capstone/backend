package org.example.user.presentation.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.user.domain.dto.request.member.AddUserRequest;
import org.example.user.application.member.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "user-api-controller", description = "회원가입, 로그아웃을 위한 컨트롤러")
@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;


    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request); // 회원가입 메서드 호출
        return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        return "redirect:/login";
    }




}
