package org.example.user.presentation.member;
import jakarta.servlet.http.HttpServletRequest;
import org.example.util.CookieUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class CookieController {

    @GetMapping("/set-cookie")
    public String setCookie(HttpServletResponse response) {
        CookieUtil.addCookie(response, "testCookie", "testValue", 3600);
        return "Cookie Set";
    }

    @GetMapping("/delete-cookie")
    public String deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "testCookie");
        return "Cookie Deleted";
    }
}
