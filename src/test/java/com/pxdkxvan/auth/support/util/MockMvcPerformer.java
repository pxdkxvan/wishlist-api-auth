package com.pxdkxvan.auth.support.util;

import com.pxdkxvan.auth.support.mapper.ResponseDTOMapper;

import jakarta.servlet.http.Cookie;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class MockMvcPerformer {

    private final MockMvc mockMvc;
    private final ResponseDTOMapper responseDtoMapper;

    public MvcResult performWithGetRequest(String uri, Map<String, String> params) throws Exception {
        return mockMvc
                .perform(get(uri)
                .queryParams(MultiValueMap.fromSingleValue(params)))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult performWithPostRequest(String uri, Object requestDTO) throws Exception {
        return mockMvc
                .perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseDtoMapper.toJSON(requestDTO)))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult performWithPostRequestAndResponseCookie(String uri, Object requestDTO, String cookieValue) throws Exception {
        return mockMvc
                .perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseDtoMapper.toJSON(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString(cookieValue)))
                .andReturn();
    }

    public MvcResult performWithPostRequestAndRequestCookie(String uri, Cookie cookie) throws Exception {
        return mockMvc
                .perform(post(uri)
                .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult performWithGetRequestAndJwtBearer(String uri, String jwtValue) throws Exception {
        return mockMvc
                .perform(get(uri)
                .with(jwt().jwt(JWT -> JWT.tokenValue(jwtValue))))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult performWithPostRequestAndJwtBearer(String uri, Object requestDTO, String jwtValue) throws Exception {
        return mockMvc
                .perform(post(uri)
                .with(jwt().jwt(JWT -> JWT.tokenValue(jwtValue)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseDtoMapper.toJSON(requestDTO)))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult performWithOAuth2GetRequestAndResponseCookie(String uri, Map<String, Object> oauth2Attrs, String cookieValue) throws Exception {
        return mockMvc
                .perform(get(uri)
                .with(oauth2Login().attributes(attrs -> attrs.putAll(oauth2Attrs))))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString(cookieValue)))
                .andReturn();
    }

}
