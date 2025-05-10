package com.bannrx.common.dtos.device;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRegistration {
    @NotNull(message = "Dimension should not be null or empty")
    @Valid
    private DimensionDto dimension;

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
