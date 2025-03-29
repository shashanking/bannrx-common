package com.bannrx.common.persistence.entities;
import com.bannrx.common.persistence.Persist;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import rklab.utility.utilities.JsonUtils;



@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    @JsonIgnore
    public String getPrefix() {
        return "AD";
    }

    public String toString(){
        return JsonUtils.jsonOf(this);
    }
}
