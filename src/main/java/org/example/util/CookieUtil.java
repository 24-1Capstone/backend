package org.example.util;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    // 요청값(이름, 값, 만료기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .build();
//        response.addHeader("Set-Cookie", cookie.toString());
//        Cookie cookie = new Cookie(name, value);
//        cookie.setPath("/");
//        cookie.setMaxAge(maxAge);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setAttribute("SameSite", "None");

//        response.addCookie(cookie);

        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        if (cookie == null || cookie.getValue() == null) {
            return null;
        }
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
//    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length > 0) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    return Optional.of(cookie);
//                }
//            }
//        }
//        return Optional.empty();
//    }

//    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setAttribute("SameSite", "None");
//        response.addCookie(cookie);
//        ResponseCookie cookie = ResponseCookie.from(name, value)
//                .path("/")
//                .sameSite("None")
//                .httpOnly(false)
//                .secure(false)
//                .maxAge(maxAge)
//                .build();
//        response.addHeader("Set-Cookie", cookie.toString());
    }

//    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length > 0) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(name)) {
//                    cookie.setValue("");
//                    cookie.setPath("/");
//                    cookie.setHttpOnly(true);
//                    cookie.setSecure(true);
//                    cookie.setAttribute("SameSite", "None");
//                    cookie.setMaxAge(0);
//                    response.addCookie(cookie);
//                }
//            }
//        }
//    }
//
//    public static String serialize(Object object) {
//        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
//    }
//    // 쿠키를 역직렬화해 객체로 변환
//    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
//        if (cookie == null || cookie.getValue() == null) {
//            return null;
//        }
//        return cls.cast(
//                SerializationUtils.deserialize(
//                        Base64.getUrlDecoder().decode(cookie.getValue())
//                )
//        );
//    }
