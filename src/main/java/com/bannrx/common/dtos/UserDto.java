package com.bannrx.common.dtos;

import com.bannrx.common.persistence.entities.User;
import com.bannrx.common.validationGroups.AddOrderValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

import static rklab.utility.constants.GlobalConstants.RegexPattern.PHONE_NO_REGEX;


@Data
@NoArgsConstructor
public class UserDto {

    @NotEmpty(groups = AddOrderValidationGroup.class, message = "User Id is mandatory.")
    private String id;

    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    private String name;

    @NotBlank(message = "Phone no cannot be blank")
    @Pattern(regexp = PHONE_NO_REGEX, message = "Phone no must be 10 digits.")
    private String phoneNo;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must be a valid email address.")
    private String email;

    @NotNull(message = "Address list cannot be null.")
    @Size(min = 1, message = "At least one address is required.")
    @Valid
    private List<AddressDto> addressDtoList;

    @NotNull(message = "Bank details list cannot be null.")
    @Size(min = 1, message = "At least one bank detail is required.")
    @Valid
    private List<BankDetailsDto> bankDetailsDtoList;

    @NotNull(message = "Business details cannot be null.")
    @Valid
    private BusinessDto businessDto;

    public UserDto(String id){
        this.id = id;
    }

    public static UserDto parse(User user){
        var mapper = new ModelMapper();
        return  mapper.map(user, UserDto.class);
    }

}
