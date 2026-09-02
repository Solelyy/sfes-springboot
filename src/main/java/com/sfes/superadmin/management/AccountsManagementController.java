package com.sfes.superadmin.management;

import com.sfes.common.classes.ApiResponse;
import com.sfes.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class AccountsManagementController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public ApiResponse<AccountsResponse> getAllAccounts(
            @RequestParam(required = false) Role type
    ) {
        return new ApiResponse<>(
                "Successfully retrieved accounts",
                accountService.getAccounts(type)
        );
    }
}
