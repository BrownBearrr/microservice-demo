package com.example.auth_service.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig { // đăng nhập vào account hệ thống keycloak để quản lý user, role, group, client,... thông qua API của keycloak admin client

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm ;

    @Value("${keycloak.resource}")
    private String clientId ;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret ;

    @Bean
    public Keycloak keycloak () {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build() ;

    }

}
