package com.bannrx.common.searchCriteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import rklab.utility.models.searchCriteria.PageableSearchCriteria;
import java.time.LocalDate;



@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CampaignSearchCriteria extends PageableSearchCriteria {
    private String name;
    @JsonFormat(pattern = "dd/MM/yyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd/MM/yyy")
    private LocalDate endDate;


    @Builder(builderMethodName = "campaignSearchCriteriaBuilder")
    public CampaignSearchCriteria(
            final String name,
            final LocalDate startDate,
            final LocalDate endDate,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Sort.Direction sortDirection
    ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
