package com.bannrx.common.dtos.campaign;

import com.bannrx.common.enums.Phase;
import com.bannrx.common.validationGroups.UpdateValidationGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;


@Data
public class CampaignDto {

    @NotEmpty(message = "Campaign Id is mandatory.", groups = UpdateValidationGroup.class)
    private String id;

    @NotBlank(message = "Name is required")
    @Size(max = 225, message = "Name must not exceed 225 characters")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
}
