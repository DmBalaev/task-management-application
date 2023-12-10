package com.dm.taskapp.auth;

public interface AuthService {
    AuthResponse signup(RegistrationRequest request);

    AuthResponse signin(AuthRequest request);
}
