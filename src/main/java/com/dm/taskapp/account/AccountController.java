package com.dm.taskapp.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account management")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @Operation(
            tags = "Account management",
            summary = "Return all user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all users"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    public ResponseEntity<Collection<AccountInfo>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllUsers());
    }

    @GetMapping("/{username}")
    @Operation(
            tags = "Account management",
            summary = "Return user by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get user "),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "The user with the same id does not exist ", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    public ResponseEntity<?> getAccountByName(@PathVariable String username){
        return ResponseEntity.ok(accountService.getUserByName(username));
    }
}