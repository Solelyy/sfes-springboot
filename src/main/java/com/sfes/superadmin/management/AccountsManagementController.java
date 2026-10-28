package com.sfes.superadmin.management;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super-admin/accounts")
@RequiredArgsConstructor
public class AccountsManagementController {
    private final AccountService accountService;

    @GetMapping
    public ApiResponse<AccountsResponse> getAllAccounts(
            @RequestParam(required = false) Role role
    ) {
        return new ApiResponse<>(
                "Successfully retrieved accounts",
                accountService.getAccounts(role)
        );
    }

    @PatchMapping("/status")
    public ApiMessage updateAccountStatus(@RequestBody UpdateStatusRequest request)
    {
        accountService.updateAccountStatus(request.employeeId(), request.status());
        return new ApiMessage("Account status updated successfully");
    }
}
