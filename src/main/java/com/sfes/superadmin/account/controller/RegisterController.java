package com.sfes.superadmin.account.controller;

import com.sfes.common.classes.ApiResponse;
import com.sfes.superadmin.account.dto.RegisterRequest;
import com.sfes.superadmin.account.service.EmailActivationService;
import com.sfes.superadmin.account.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/superadmin/")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;
    private final EmailActivationService emailActivationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerAccount(@Valid  @RequestBody RegisterRequest request) {
        String rawToken = registerService.registerUser(request);

        emailActivationService.sendActivationEmail(request.email(), request.firstName(), rawToken);

        return ResponseEntity.ok(new ApiResponse("Account successfully created."));
    }
}
