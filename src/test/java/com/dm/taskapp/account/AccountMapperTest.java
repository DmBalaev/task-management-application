package com.dm.taskapp.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {
    @Test
    public void apply_ShouldMapAccountToAccountInfo() {
        // Arrange
        AccountMapper accountMapper = new AccountMapper();
        Account account = Account.builder()
                .id(1L)
                .name("test")
                .email("john@example.com")
                .build();

        AccountInfo accountInfo = accountMapper.apply(account);

        assertEquals(account.getId(), accountInfo.id());
        assertEquals(account.getName(), accountInfo.name());
        assertEquals(account.getEmail(), accountInfo.email());
    }
}