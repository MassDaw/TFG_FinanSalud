package com.proyecto.tfg_finansalud.DTO.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class TokenResponse {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("refresh_token")
    String refreshToken;
}
