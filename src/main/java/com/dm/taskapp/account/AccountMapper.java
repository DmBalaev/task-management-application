package com.dm.taskapp.account;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AccountMapper implements Function<Account, AccountInfo> {

    @Override
    public AccountInfo apply(Account account) {
        return new AccountInfo(
                account.getId(),
                account.getName(),
                account.getEmail()
        );
    }
}