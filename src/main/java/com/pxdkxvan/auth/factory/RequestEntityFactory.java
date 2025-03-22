package com.pxdkxvan.auth.factory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public final class RequestEntityFactory {

    private static final String BEARER_PREFIX = "Bearer ";

    public RequestEntity<Void> createGet(String uri) {
        return RequestEntity
                .method(HttpMethod.GET, uri)
                .build();
    }

    public RequestEntity<Void> createGet(String uri, String authToken) {
        return RequestEntity
                .method(HttpMethod.GET, uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + authToken)
                .build();
    }

    public <T> RequestEntity<T> createPost(String uri, T dto) {
        return RequestEntity
                .method(HttpMethod.POST, uri)
                .body(dto);
    }

    public <T> RequestEntity<T> createPost(String uri, String authToken, T dto) {
        return RequestEntity
                .method(HttpMethod.POST, uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + authToken)
                .body(dto);
    }

    public <T> RequestEntity<T> createPost(String uri, ResponseCookie cookie, T dto) {
        return RequestEntity
                .method(HttpMethod.POST, uri)
                .header(HttpHeaders.COOKIE, cookie.toString())
                .body(dto);
    }

}
