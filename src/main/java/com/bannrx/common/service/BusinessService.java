package com.bannrx.common.service;

import com.bannrx.common.dtos.BusinessDto;
import com.bannrx.common.persistence.entities.Business;
import com.bannrx.common.repository.BusinessRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ObjectMapperUtils;
import java.util.Objects;



@Service
@Loggable
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserService userService;
    public BusinessService(BusinessRepository businessRepository, @Lazy UserService userService) {
        this.businessRepository = businessRepository;
        this.userService = userService;
    }

    public Business toEntity(BusinessDto businessDto) throws ServerException {
        return ObjectMapperUtils.map(businessDto, Business.class);
    }

    /**
     * To dto business dto.
     *
     * @param business the business
     * @return the business dto
     * @throws ServerException the server exception
     */
    public BusinessDto toDto(Business business) throws ServerException {
        if (Objects.nonNull(business)){
            return ObjectMapperUtils.map(business, BusinessDto.class);
        }
        return null;
    }

    public BusinessDto update(BusinessDto businessDto) throws ServerException, InvalidInputException {
        var business = fetchById(businessDto.getId());
        ObjectMapperUtils.map(businessDto, business);
        business = businessRepository.save(business);
        return toDto(business);
    }

    private Business fetchById(String id) throws InvalidInputException {
        return businessRepository.findById(id)
                .orElseThrow(()-> new InvalidInputException(
                        String.format("Business Details is not found with Id %s ", id))
                );
    }

    public void validate(BusinessDto businessDto, String loggedInUserId) throws InvalidInputException {
        var user = userService.fetchById(loggedInUserId);
        var businessId = user.getBusiness().getId();
        if (!StringUtils.equals(businessId, businessDto.getId())){
            throw new UnsupportedOperationException("Business Details are associated to other user.");
        }
    }

}
