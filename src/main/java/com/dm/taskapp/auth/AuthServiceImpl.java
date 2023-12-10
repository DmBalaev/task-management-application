package com.dm.taskapp.auth;

import com.dm.taskapp.account.Account;
import com.dm.taskapp.account.AccountRepository;
import com.dm.taskapp.account.Role;
import com.dm.taskapp.exceptions.ApiException;
import com.dm.taskapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(RegistrationRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())){
            throw new ApiException("Username is already taken", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var user = Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();

        accountRepository.save(user);
        var jwt = jwtService.generateToken(user);

        return AuthResponse.builder().token(jwt).build();
    }

    @Override
    public AuthResponse signin(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwt).build();
    }
}
