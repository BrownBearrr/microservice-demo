package com.example.auth_service.controller;

import com.example.auth_service.dto.request.UserLoginDto;
import com.example.auth_service.dto.request.UserRefreshTokenDto;
import com.example.auth_service.dto.request.UserRegistrationDto;
import com.example.auth_service.dto.request.UserResetPassworDto;
import com.example.auth_service.dto.response.ApiResponse;
import com.example.auth_service.dto.response.KeycloakTokenResponse;
import com.example.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService ;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserRegistrationDto dto) {
        userService.createUser(dto);

        return ResponseEntity.ok().body(new ApiResponse<Void>(201, "User registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<KeycloakTokenResponse>> login(@RequestBody UserLoginDto dto) {
        KeycloakTokenResponse tokenResponse = userService.login(dto);

        return ResponseEntity.ok().body(new ApiResponse<>(200, "Login successful", tokenResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<KeycloakTokenResponse>> refresh(@RequestBody UserRefreshTokenDto refreshToken) {
        KeycloakTokenResponse tokenResponse = userService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(new ApiResponse<>(200, "Refresh Token successful", tokenResponse));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody UserResetPassworDto dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok().body(new ApiResponse<>(200, "Reset password successfully", null));
    }

}
