package com.bannrx.common.dtos.device;

import com.bannrx.common.validationGroups.UpdateValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceDto {

    @NotEmpty(message = "Device Id is mandatory.", groups = UpdateValidationGroup.class)
    private String id;

    @NotNull(message = "Dimension should not be null or empty")
    @Valid
    private DimensionUpdateDto dimension;

    @NotNull(message = "isFrontCameraAvailable should not be null")
    private Boolean isFrontCameraAvailable;

    @NotNull(message = "isBackCameraAvailable should not be null")
    private Boolean isBackCameraAvailable;

    @NotNull(message = "isWorking should not be null")
    private Boolean isWorking;

    @NotNull(message = "Active field should not be null")
    private Boolean active;

    @Size(max = 255, message = "Remarks should not exceed 255 characters")
    private String remarks;
}
