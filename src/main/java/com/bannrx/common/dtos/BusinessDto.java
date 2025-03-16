package com.bannrx.common.dtos;

import com.bannrx.common.enums.BusinessType;
import com.bannrx.common.validationGroups.AddOrderValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDto {

    @NotEmpty(groups = AddOrderValidationGroup.class, message = "Business Id is mandatory.")
    private String id;

    @NotBlank(message = "Business name cannot be blank.")
    @Size(max = 100, message = "Business name must be less than 100 characters.")
    private String name;

    @NotNull(message = "Business type cannot be null.")
    private BusinessType type;
}
