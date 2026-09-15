package com.sfes.registrar.department;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.department.dto.CreateDepartment;
import com.sfes.registrar.department.dto.DepartmentResponse;
import com.sfes.registrar.department.dto.UpdateDepartment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/departments")
    public ApiMessage createDepartment(@Valid @RequestBody CreateDepartment request) {
        departmentService.createDepartment(request.departmentName(), request.departmentCode());

        return  new ApiMessage("Department created successfully");
    }

    @PatchMapping("/departments/update")
    public ApiMessage updateDepartmentStatus(@Valid @RequestBody UpdateDepartment request) {
        departmentService.updateDeptStatus(request.departmentCode(), request.departmentStatus());

        return new ApiMessage("Department status updated successfully");
    }

    @GetMapping("/departments")
    public ApiResponse<DepartmentResponse> getDepartments() {
        return new ApiResponse<>(
                "Retrieved departments successfully",
                departmentService.getDepartments()
        );
    }
}
