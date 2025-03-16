package com.bannrx.common.persistence.entities;
import com.bannrx.common.persistence.Persist;
import jakarta.persistence.*;
import lombok.*;
import rklab.utility.utilities.JsonUtils;



@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table
public class Address extends Persist {

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name="address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String getPrefix() {
        return "AD";
    }

    public String toString(){
        return JsonUtils.jsonOf(this);
    }
}
