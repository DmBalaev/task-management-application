package com.dm.taskapp.account;

import com.dm.taskapp.exceptions.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    private final AccountMapper accountConvector;

    @Override
    public AccountInfo getUserByName(String name) {
        return accountRepository.findByEmail(name)
                .map(accountConvector)
                .orElseThrow(()-> new ApiException("Resource not found", HttpStatus.BAD_REQUEST));
    }

    @Override
    public Collection<AccountInfo> getAllUsers() {
        return accountRepository.findAll().stream()
                .map(accountConvector)
                .toList();
    }
}
