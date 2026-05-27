package com.example.auth_service.service;

import com.example.auth_service.dto.request.UserLoginDto;
import com.example.auth_service.dto.request.UserRefreshTokenDto;
import com.example.auth_service.dto.request.UserRegistrationDto;
import com.example.auth_service.dto.request.UserResetPassworDto;
import com.example.auth_service.dto.response.KeycloakTokenResponse;

public interface UserService {
    void createUser(UserRegistrationDto dto) ;
    void resetPassword(UserResetPassworDto dto) ;
    KeycloakTokenResponse login(UserLoginDto dto) ;
    KeycloakTokenResponse refreshToken(UserRefreshTokenDto dto) ;
}
