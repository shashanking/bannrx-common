package com.bannrx.common.dtos.device;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;



@Data
public class DimensionUpdateDto {
    @NotNull(message = "length cannot be null")
    @Positive(message = "Length must be positive")
    private Number length;

    @NotNull(message = "breadth cannot be null")
    @Positive(message = "Breadth must be positive")
    private Number breadth;

    @NotNull(message = "Unit cannot be null")
    @Pattern(regexp = "mm|cm|m|in", message = "Unit must be one of: mm, cm, m, in")
    private String unit;
}
