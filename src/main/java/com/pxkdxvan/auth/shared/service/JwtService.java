package com.pxkdxvan.auth.shared.service;

import com.pxkdxvan.auth.shared.properties.JwtProperties;
import com.pxkdxvan.auth.shared.security.JwtType;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtService {

    private static final String JWT_TYPE = "JWT";
    private static final String JWT_ISSUER = "auth-service";

    private final Duration ACCESS_TOKEN_DURATION;
    private final Duration REFRESH_TOKEN_DURATION;
    private final Duration TEMPORARY_TOKEN_DURATION;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    JwtService(JwtProperties jwtProperties, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        ACCESS_TOKEN_DURATION = jwtProperties.access().duration();
        REFRESH_TOKEN_DURATION = jwtProperties.refresh().duration();
        TEMPORARY_TOKEN_DURATION = jwtProperties.temporary().duration();
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(JwtType type, String jwtSubject) {
        Duration tokenDuration = switch (type) {
            case ACCESS -> ACCESS_TOKEN_DURATION;
            case REFRESH -> REFRESH_TOKEN_DURATION;
            case TEMPORARY -> TEMPORARY_TOKEN_DURATION;
        };

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .type(JWT_TYPE)
                .build();

        Instant now = Instant.now();

        JwtClaimsSet payload = JwtClaimsSet
                .builder()
                .issuer(JWT_ISSUER)
                .subject(jwtSubject)
                .issuedAt(now)
                .expiresAt(now.plus(tokenDuration))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(header, payload))
                .getTokenValue();
    }

    public String extractSubject(String token) {
        return jwtDecoder
                .decode(token)
                .getSubject();
    }

    public Instant extractExpiresAt(String token) {
        return jwtDecoder
                .decode(token)
                .getExpiresAt();
    }

    public String extractClaim(String token, String claim) {
        return jwtDecoder
                .decode(token)
                .getClaim(claim);
    }

}
