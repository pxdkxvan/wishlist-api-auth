package com.pxkdxvan.auth.shared.factory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public final class ResponseEntityFactory {

    public <T> ResponseEntity<T> create(HttpStatus status, T dto) {
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    public <T> ResponseEntity<T> create(HttpStatus status, ResponseCookie cookie, T dto) {
        return ResponseEntity
                .status(status)
                .header(HttpHeaders.SET_COOKIE, cookie.getValue())
                .body(dto);
    }

}
