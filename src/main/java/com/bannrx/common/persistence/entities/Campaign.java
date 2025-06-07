package com.bannrx.common.persistence.entities;

import com.bannrx.common.enums.Phase;
import com.bannrx.common.persistence.Persist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;




@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "campaign", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_name_date", columnList = "name, start_date, end_date")
})
public class Campaign extends Persist {

    @Column(name = "name", length = 225, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Phase phase;


    @Override
    public String getPrefix() {
        return "CM";
    }
}
