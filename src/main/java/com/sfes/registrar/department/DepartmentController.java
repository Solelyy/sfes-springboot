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
@RequestMapping("/registrar/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public ApiMessage createDepartment(@Valid @RequestBody CreateDepartment request) {
        departmentService.createDepartment(request.departmentName(), request.departmentCode());

        return  new ApiMessage("Department created successfully");
    }

    @PatchMapping("/{id}")
    public ApiMessage updateDepartmentStatus(@PathVariable Long id, @Valid @RequestBody UpdateDepartment request) {
        departmentService.updateDeptStatus(id, request);

        return new ApiMessage("Department status updated successfully");
    }

    @GetMapping
    public ApiResponse<DepartmentResponse> getDepartments() {
        return new ApiResponse<>(
                "Retrieved departments successfully",
                departmentService.getDepartments()
        );
    }
}