package com.example.auth_service.service.impl;


import com.example.auth_service.dto.request.UserLoginDto;
import com.example.auth_service.dto.request.UserRefreshTokenDto;
import com.example.auth_service.dto.request.UserRegistrationDto;
import com.example.auth_service.dto.request.UserResetPassworDto;
import com.example.auth_service.dto.response.KeycloakTokenResponse;
import com.example.auth_service.service.UserService;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;

    private final WebClient webClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Override
    public void createUser(UserRegistrationDto dto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());
        user.setCredentials(Collections.singletonList(credential));

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            log.info("Status: {}", response.getStatus());

            log.info("Headers: {}", response.getHeaders());
            String errorMessage =response.readEntity(Map.class).get("errorMessage").toString();
            log.error("Create user failed: {}", errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public void resetPassword(UserResetPassworDto dto) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getNewPassword());
        try {
            UserResource  userResource = keycloak.realm(realm).users().get(dto.getUserId());
            userResource.resetPassword(credential);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public KeycloakTokenResponse login(UserLoginDto dto) {
        String tokenUrl = serverUrl +"/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>();

        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username",dto.getUsername());
        formData.add("password", dto.getPassword());

        KeycloakTokenResponse tokenResponse = webClient
                .post()
                .uri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .block();

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Login failed");
        }

        return tokenResponse;
    }

    @Override
    public KeycloakTokenResponse refreshToken(UserRefreshTokenDto dto) {
        String tokenUrl = serverUrl +"/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>();

        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token",dto.getRefreshToken());


        KeycloakTokenResponse tokenResponse = webClient
                .post()
                .uri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class)
                .block();

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("get token failed");
        }

        return tokenResponse;
    }
}
