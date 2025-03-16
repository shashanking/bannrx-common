package com.bannrx.common.dtos;

import com.bannrx.common.validationGroups.AddOrderValidationGroup;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static rklab.utility.constants.GlobalConstants.RegexPattern.IFSC_REGEX;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsDto {

    @NotEmpty(groups = AddOrderValidationGroup.class, message = "Bank Details Id is mandatory.")
    private String id;

    @NotNull(message = "Account number cannot be null")
    private Long accountNo;

    @NotBlank(message = "IFSC code cannot be blank")
    @Size(min = 11, max = 11, message = "IFSC code must be 11 characters long")
    @Pattern(regexp = IFSC_REGEX, message = "IFSC code must be in the format AAAA0XXXXXX (4 letters, 0, 6 alphanumeric characters)")
    private String ifscCode;

    @NotNull(message = "Verified status cannot be null")
    private Boolean verified;

    @NotBlank(message = "Verification process ID cannot be blank")
    private String verificationProcessId;
}
