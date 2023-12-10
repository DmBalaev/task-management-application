package com.dm.taskapp.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    private static final String EMAIL = "test@example.com";
    private static final String NAME = "test";
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void getUserByName_ExistingUser_ReturnsAccountInfo() {

        Account account = new Account();
        account.setEmail(EMAIL);
        AccountInfo expectedAccountInfo = new AccountInfo(1L, "test2", EMAIL);

        when(accountRepository.findByEmail(EMAIL)).thenReturn(Optional.of(account));
        when(accountMapper.apply(account)).thenReturn(expectedAccountInfo);

        AccountInfo result = accountService.getUserByName(EMAIL);

        assertEquals(expectedAccountInfo, result);
        verify(accountRepository, times(1)).findByEmail(EMAIL);
        verify(accountMapper, times(1)).apply(account);
    }

    @Test
    void getAllUsers_ReturnsListOfAccountInfo() {
        List<Account> accounts = List.of(new Account(), new Account());
        List<AccountInfo> expectedAccountInfos = List.of(new AccountInfo(1L, "test2", EMAIL), new AccountInfo(1L, "test2", EMAIL));

        when(accountRepository.findAll()).thenReturn(accounts);
        when(accountMapper.apply(any(Account.class))).thenReturn(expectedAccountInfos.get(0), expectedAccountInfos.get(1));

        Collection<AccountInfo> result = accountService.getAllUsers();

        assertEquals(expectedAccountInfos, result);
        verify(accountRepository, times(1)).findAll();
        verify(accountMapper, times(2)).apply(any());
    }
}