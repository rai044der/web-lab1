package ru.abradox.carsbusinesscard.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLogin() {
        var context = SecurityContextHolder.getContext();
        var isAuthenticated = Optional.ofNullable(context.getAuthentication())
                .filter(Authentication::isAuthenticated)
                .isPresent();
        return ResponseEntity.ok(isAuthenticated);
    }
}