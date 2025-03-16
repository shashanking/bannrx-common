package com.bannrx.common.dtos;

import com.bannrx.common.validationGroups.AddOrderValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class AddressDto {

    @NotEmpty(groups = AddOrderValidationGroup.class, message = "Address Id is mandatory")
    private String id;

    @NotBlank(message = "Address line 1 cannot be empty")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "State cannot be empty")
    private String state;

    @NotBlank(message = "Pin code cannot be empty")
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

