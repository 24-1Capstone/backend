package org.example.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {
    @GetMapping("/test")
    public String test() {
        throw new IllegalArgumentException("Test exception");
    }
}
