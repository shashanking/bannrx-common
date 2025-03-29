package com.bannrx.common.dtos;
import com.bannrx.common.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import static rklab.utility.constants.GlobalConstants.RegexPattern.PHONE_NO_REGEX;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    private String name;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = PHONE_NO_REGEX, message = "Phone no must be 10 digits.")
    private String phoneNo;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;

    @NotNull(message = "Role cannot be null.")
    private UserRole role;

    @NotNull(message = "Address list cannot be null.")
    @Size(min = 1, message = "At least one address is required.")
    @Valid
    private Set<AddressDto> addressDtoSet;

    @NotNull(message = "Bank details list cannot be null.")
    @Size(min = 1, message = "At least one bank detail is required.")
    @Valid
    private Set<BankDetailsDto> bankDetailsDtoSet;

    @NotNull(message = "Business details cannot be null.")
    @Valid
    private BusinessDto businessDto;

}
