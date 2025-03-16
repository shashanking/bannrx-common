package com.bannrx.common.persistence.entities;

import com.bannrx.common.persistence.Persist;
import com.bannrx.common.enums.BusinessType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import rklab.utility.utilities.JsonUtils;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Business extends Persist {

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BusinessType type;

    @Override
    public String getPrefix() {
        return "BU";
    }

    public String toString(){
        return JsonUtils.jsonOf(this);
    }

}
