package com.bannrx.common.service;

import com.bannrx.common.dtos.BankDetailsDto;
import com.bannrx.common.enums.Status;
import com.bannrx.common.persistence.entities.BankDetails;
import com.bannrx.common.repository.BankDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rklab.utility.annotations.Loggable;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ObjectMapperUtils;
import java.util.List;


@Service
@Loggable
@RequiredArgsConstructor
public class BankDetailsService {

    private final BankDetailsRepository bankDetailsRepository;;

    @Transactional
    public BankDetails save(List<BankDetailsDto> bankDetailsDtoList) throws ServerException {

        for(var bankDetailList : bankDetailsDtoList){
            var retVal = ObjectMapperUtils.map(bankDetailList, BankDetails.class);
            retVal.setStatus(Status.ACTIVE);
            return bankDetailsRepository.save(retVal);
        }

        throw new ServerException("Only one bank details you can add.");
    }
}
