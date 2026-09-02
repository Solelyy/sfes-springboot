package com.sfes.superadmin.management;

import com.sfes.employee.entity.Employee;
import com.sfes.employee.repository.EmployeeRepository;
import com.sfes.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final EmployeeRepository employeeRepository;

    public AccountsResponse getAccounts(Role type) {
        List<AccountsResponse.Account> accounts =
                employeeRepository.findAll()
                        .stream()
                        .filter(employee -> employee.getUser().getRole() != Role.SUPER_ADMIN)
                        .filter(employee ->
                                type == null || employee.getUser().getRole() == type)
                        .sorted(Comparator.comparing(Employee::getCreatedAt,
                                Comparator.reverseOrder()))
                        .map(employee -> new AccountsResponse.Account(
                                employee.getEmployeeId(),
                                employee.getUser().getEmail(),
                                employee.getFirstName(),
                                employee.getMiddleName(),
                                employee.getLastName(),
                                employee.getUser().getRole(),
                                employee.getUser().getStatus()
                        ))
                        .toList();

        return new AccountsResponse(accounts);
    }

}
