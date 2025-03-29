package com.bannrx.common.persistence.entities;

import com.bannrx.common.enums.UserRole;
import com.bannrx.common.persistence.Persist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.JsonUtils;
import java.util.*;
import java.util.List;



@EqualsAndHashCode(callSuper = true, exclude = {"addresses", "bankDetails", "business"})
@Data
@Entity
public class User extends Persist implements UserDetails {

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

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BankDetails> bankDetails = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @Override
    public String setDefaultModifiedBy(){
        var retVal = super.setDefaultModifiedBy();
        if (StringUtils.isBlank(retVal)){
            retVal = this.email;
        }
        return retVal;
    }

    @JsonIgnore
    public void addAddreses(Set<Address> addresses) throws ServerException {

        if (addresses == null) {
            throw new ServerException("Addresses should not be null.");
        }

        if (this.addresses == null) {
            this.addresses = new HashSet<>();
        }

        this.addresses.clear();
        for (var address : addresses) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }

    @JsonIgnore
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

    @JsonIgnore
    public void addBankDetails(Set<BankDetails> bankDetails) throws ServerException {

        if(bankDetails == null){
           throw new ServerException("Bank details should not be null");
        }

        if (this.bankDetails == null) {
            this.bankDetails = new HashSet<>();
        }

        this.bankDetails.clear();
        for (var bankDetail : bankDetails) {
            this.bankDetails.add(bankDetail);
            bankDetail.setUser(this);
        }
    }

    @JsonIgnore
    public void removeBankDetail(BankDetails bankDetail) {
        bankDetails.remove(bankDetail);
        bankDetail.setUser(null);
    }

    @Override
    @JsonIgnore
    public String getPrefix() {
        return "UR";
    }

    public String toString(){
        return JsonUtils.jsonOf(this);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.getId();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
