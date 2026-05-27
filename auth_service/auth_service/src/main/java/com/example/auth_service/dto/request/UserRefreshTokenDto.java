package com.example.auth_service.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRefreshTokenDto {
    private String refreshToken ;
}
