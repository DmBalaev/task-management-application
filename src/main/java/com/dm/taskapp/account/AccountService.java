package com.dm.taskapp.account;

import java.util.Collection;

public interface AccountService {
    AccountInfo getUserByName(String username);
    Collection<AccountInfo> getAllUsers();
}
