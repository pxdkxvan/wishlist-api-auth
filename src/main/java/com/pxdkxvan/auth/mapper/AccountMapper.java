package com.pxdkxvan.auth.mapper;

import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.security.AccountDetails;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", source = "authorization.account.id")
    @Mapping(target = "username", source = "authorization.account.username")
    @Mapping(target = "email", source = "authorization.account.email")
    @Mapping(target = "verified", source = "authorization.account.verified")
    @Mapping(target = "roles", source = "authorization.account.roles")
    AccountDetails toAccountDetails(Authorization authorization);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authorizations", ignore = true)
    @Mapping(target = "verifications", ignore = true)
    Account toAccount(AccountDetails account);

}
