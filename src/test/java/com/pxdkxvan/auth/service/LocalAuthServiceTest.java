package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.config.MapperConfig;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.exception.auth.EmailAlreadyTakenException;
import com.pxdkxvan.auth.exception.auth.UsernameAlreadyTakenException;
import com.pxdkxvan.auth.mapper.AccountMapper;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.security.AccountDetails;
import com.pxdkxvan.auth.util.EmailMasker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {MapperConfig.class, LocalAuthService.class, TestEntityFactory.class})
class LocalAuthServiceTest {

    private static final Provider PROVIDER = Provider.LOCAL;

    private final AccountMapper accountMapper;

    @MockitoBean
    private final AuthenticationManager authenticationManager;

    @MockitoBean
    private final AccountService accountService;
    @MockitoBean
    private final AuthorizationService authorizationService;
    private final LocalAuthService localAuthService;

    private final TestEntityFactory testEntityFactory;

    @ParameterizedTest
    @ValueSource(strings = {ACCOUNT_USERNAME, ACCOUNT_EMAIL})
    void loginSuccess(String login) {
        Authorization testAuthorization = testEntityFactory.createAuthorization(PROVIDER);
        AccountDetails testAccountDetails = accountMapper.toAccountDetails(testAuthorization);
        Account testAccount = testAuthorization.getAccount();

        Authentication authenticationToken = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticationToken);
        when(authenticationToken.getPrincipal()).thenReturn(testAccountDetails);

        assertThat(localAuthService.login(login, ACCOUNT_PASSWORD)).isEqualTo(testAccount);

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(authenticationToken).getPrincipal();
    }

    @Test
    void registerSuccess() {
        Authorization testAuthorization = testEntityFactory.createAuthorization(PROVIDER);
        Account testAccount = testAuthorization.getAccount();

        when(accountService.isUsernameTaken(ACCOUNT_USERNAME)).thenReturn(false);
        when(accountService.isEmailTaken(ACCOUNT_EMAIL)).thenReturn(false);

        when(accountService.createDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL)).thenReturn(testAccount);
        when(authorizationService.createLocalAuth(testAccount, ACCOUNT_PASSWORD)).thenReturn(testAuthorization);

        assertThat(localAuthService.register(ACCOUNT_USERNAME, ACCOUNT_EMAIL, ACCOUNT_PASSWORD)).isEqualTo(testAccount);

        verify(accountService).isUsernameTaken(ACCOUNT_USERNAME);
        verify(accountService).isEmailTaken(ACCOUNT_EMAIL);

        verify(accountService).createDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL);
        verify(authorizationService).createLocalAuth(testAccount, ACCOUNT_PASSWORD);
    }

    @Test
    void registerFailedUsernameTaken() {
        when(accountService.isUsernameTaken(ACCOUNT_USERNAME)).thenReturn(true);

        assertThatThrownBy(() -> localAuthService.register(ACCOUNT_USERNAME, ACCOUNT_EMAIL, ACCOUNT_PASSWORD))
                .isInstanceOf(UsernameAlreadyTakenException.class)
                .extracting(e -> ((UsernameAlreadyTakenException) e).getIdentifier())
                .isEqualTo(ACCOUNT_USERNAME);

        verify(accountService).isUsernameTaken(ACCOUNT_USERNAME);
    }

    @Test
    void registerFailedEmailTaken() {
        when(accountService.isUsernameTaken(ACCOUNT_USERNAME)).thenReturn(false);
        when(accountService.isEmailTaken(ACCOUNT_EMAIL)).thenReturn(true);

        assertThatThrownBy(() -> localAuthService.register(ACCOUNT_USERNAME, ACCOUNT_EMAIL, ACCOUNT_PASSWORD))
                .isInstanceOf(EmailAlreadyTakenException.class)
                .extracting(e -> ((EmailAlreadyTakenException) e).getIdentifier())
                .isEqualTo(EmailMasker.mask(ACCOUNT_EMAIL));

        verify(accountService).isUsernameTaken(ACCOUNT_USERNAME);
        verify(accountService).isEmailTaken(ACCOUNT_EMAIL);
    }

}
