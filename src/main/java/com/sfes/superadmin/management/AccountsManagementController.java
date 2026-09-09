package com.sfes.superadmin.management;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class AccountsManagementController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public ApiResponse<AccountsResponse> getAllAccounts(
            @RequestParam(required = false) Role role
    ) {
        return new ApiResponse<>(
                "Successfully retrieved accounts",
                accountService.getAccounts(role)
        );
    }

    @PatchMapping("/accounts/{employeeId}/status")
    public ApiMessage updateAccountStatus(
            @PathVariable String employeeId,
            @RequestBody UpdateStatusRequest request)
    {
        accountService.updateAccountStatus(employeeId, request.status());
        return new ApiMessage("Account status successfully updated");
    }
}
