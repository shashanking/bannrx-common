package com.bannrx.common.dtos;

import com.bannrx.common.validationGroups.AddValidationGroup;
import com.bannrx.common.validationGroups.UpdateValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static rklab.utility.constants.GlobalConstants.RegexPattern.PIN_CODE_REGEX;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    @NotEmpty(groups = UpdateValidationGroup.class, message = "Address Id is mandatory")
    private String id;

    @NotBlank(message = "Address line 1 cannot be empty")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "State cannot be empty")
    private String state;

    @Pattern(regexp = PIN_CODE_REGEX, message = "Invalid PIN code. Must be a 6-digit number starting with 1-9.")
    @NotBlank(message = "PIN code cannot be empty")
    private String pincode;

    @NotNull(message = "Latitude can not be empty")
    private Double latitude;

    @NotNull(message = "Longitude can not be empty")
    private Double longitude;
    
    public boolean isAddressLine2Equal(String addressLine2){
        if(addressLine2==null || this.addressLine2.equalsIgnoreCase(addressLine2)){
            return true;
        }
        return false;
    }

}

