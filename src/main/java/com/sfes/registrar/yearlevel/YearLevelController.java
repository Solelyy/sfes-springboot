package com.sfes.registrar.yearlevel;

import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.yearlevel.dto.YearLevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registrar/yearlevels")
@RequiredArgsConstructor
public class YearLevelController {
    private final YearLevelService yearLevelService;

    @GetMapping("/{courseCode}")
    public ApiResponse<YearLevelResponse> getYearLevels(
            @PathVariable String courseCode
    ){

        return new ApiResponse<>(
                "Year levels successfully retrieved",
                yearLevelService.getYearLevels(courseCode)
        );
    }
}
