package com.pxkdxvan.auth.oauth2.factory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;

@Component
public final class RequestEntityFactory {

    private static final String BEARER_PREFIX = "Bearer ";

    private RequestEntity<Void> createIdempotent(HttpMethod method, String uri) {
        return RequestEntity
                .method(method, uri)
                .build();
    }

    private RequestEntity<Void> createIdempotent(HttpMethod method, String uri, String authToken) {
        return RequestEntity
                .method(method, uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + authToken)
                .build();
    }

    private <T> RequestEntity<T> createNonIdempotent(HttpMethod method, String uri, T dto) {
        return RequestEntity
                .method(method, uri)
                .body(dto);
    }

    private <T> RequestEntity<T> createNonIdempotent(HttpMethod method, String uri, String authToken, T dto) {
        return RequestEntity
                .method(method, uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + authToken)
                .body(dto);
    }

    public RequestEntity<Void> createGitHubEmailFetch(String url, String authToken) {
        return createIdempotent(HttpMethod.GET, url, authToken);
    }

}
