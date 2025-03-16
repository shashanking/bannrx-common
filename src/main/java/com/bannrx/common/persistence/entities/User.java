package com.bannrx.common.persistence.entities;

import com.bannrx.common.enums.UserRole;
import com.bannrx.common.persistence.Persist;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import rklab.utility.utilities.JsonUtils;

import java.util.ArrayList;
import java.util.List;



@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class User extends Persist {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_no", nullable = false)
    private String phoneNo;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankDetails> bankDetails = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Override
    public String setDefaultModifiedBy(){
        var retVal = super.setDefaultModifiedBy();
        if (StringUtils.isBlank(retVal)){
            retVal = this.phoneNo;
        }
        return retVal;
    }

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void addBankDetail(BankDetails bankDetail) {
        bankDetails.add(bankDetail);
        bankDetail.setUser(this);
    }

    @Override
    public String getPrefix() {
        return "UR";
    }

    public String toString(){
        return JsonUtils.jsonOf(this);
    }

}
