package com.bannrx.common.specifications;

import com.bannrx.common.persistence.entities.Campaign;
import com.bannrx.common.searchCriteria.CampaignSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.util.Objects;
import static rklab.utility.constants.GlobalConstants.Symbols.PERCENTAGE;



public class CampaignSpecification {

    private static final String NAME = "name";
    private static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";


    public static Specification<Campaign> buildSearchCriteria(final CampaignSearchCriteria searchCriteria){
        var retVal = Specification.<Campaign>where(null);

        if(StringUtils.isNotBlank(searchCriteria.getName())){
            retVal = retVal.and(filterByNameLike(searchCriteria.getName()));
        }

        if(Objects.nonNull(searchCriteria.getStartDate()) && Objects.isNull(searchCriteria.getEndDate())){
            retVal = retVal.and(filterByDate(START_DATE, searchCriteria.getStartDate()));
        }

        if(Objects.nonNull(searchCriteria.getEndDate()) && Objects.isNull(searchCriteria.getStartDate())){
            retVal = retVal.and(filterByDate(END_DATE, searchCriteria.getEndDate()));
        }

        if (Objects.nonNull(searchCriteria.getStartDate()) && Objects.nonNull(searchCriteria.getEndDate())){
            retVal = retVal.and(isCampaignRunningBetween(searchCriteria.getStartDate(), searchCriteria.getEndDate()));
        }
        return retVal;
    }

    private static Specification<Campaign> isCampaignRunningBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get(START_DATE), endDate),
                cb.greaterThanOrEqualTo(root.get(END_DATE), startDate)
        );
    }

    private static Specification<Campaign> filterByDate(String dateField, LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(dateField), date);
    }

    private static Specification<Campaign> filterByNameLike(final String name){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NAME), withLikePattern(name));
    }

    private static String withLikePattern(String s){
        return PERCENTAGE + s + PERCENTAGE;
    }
}
