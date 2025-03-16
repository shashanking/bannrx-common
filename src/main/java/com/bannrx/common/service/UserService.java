package com.bannrx.common.service;

import com.bannrx.common.dtos.*;
import com.bannrx.common.enums.Status;
import com.bannrx.common.persistence.entities.User;
import com.bannrx.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rklab.utility.annotations.Loggable;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ObjectMapperUtils;

import java.util.List;
import java.util.Optional;

@Service
@Loggable
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BusinessService businessService;
    private final BankDetailsService bankDetailsService;
    private final AddressService addressService;


    @Transactional
    public UserDto createUser(SignUpRequest request) throws ServerException, InvalidInputException {
        var retVal = new User();
        retVal = ObjectMapperUtils.map(request, User.class);
        var savedBankDetails = bankDetailsService.save(request.getBankDetailsDtoList());
        var savedAddressDetails = addressService.save(request.getAddressDtoList());
        var savedBusinessDetails = businessService.save(request.getBusinessDto());
        if(savedAddressDetails != null && savedBusinessDetails != null && savedBankDetails != null){
            retVal.addBankDetail(savedBankDetails);
            retVal.addAddress(savedAddressDetails);
            retVal.setBusiness(savedBusinessDetails);
            retVal.setStatus(Status.ACTIVE);
            retVal = userRepository.save(retVal);
            var userDto = ObjectMapperUtils.map(retVal,UserDto.class);
            var addressDto = ObjectMapperUtils.map(savedAddressDetails, AddressDto.class);
            var bankDto = ObjectMapperUtils.map(savedBankDetails, BankDetailsDto.class);
            var businessDto = ObjectMapperUtils.map(savedBusinessDetails, BusinessDto.class);
            userDto.setAddressDtoList(List.of(addressDto));
            userDto.setBankDetailsDtoList(List.of(bankDto));
            userDto.setBusinessDto(businessDto);
            return userDto;
        }
        throw new ServerException("Bank details or address details or business details might not saved");
    }

    public boolean isExistingUser(String phoneNo){
        return existingContactNo(phoneNo);
    }

    public UserDto update(UserDto userDto) throws ServerException, InvalidInputException {
        User user = fetchById(userDto.getId());
        ObjectMapperUtils.map(userDto,user);
        user = userRepository.save(user);
        return ObjectMapperUtils.map(user,UserDto.class);
    }

    public void delete(String phoneNo) {
        var user = userRepository.findByPhoneNo(phoneNo);
        userRepository.delete(user.get());
    }

    public User fetchById(String id) throws InvalidInputException {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException(
                        String.format("User not found with Id %s", id)));
    }

    public User findByPhoneNo(String phoneNo) throws InvalidInputException {
        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new InvalidInputException(
                        String.format("User not found with phone no %s", phoneNo)
                ));
    }

    public boolean existingContactNo(String phoneNo){
        Optional<User> userMayBe = userRepository.findByPhoneNo(phoneNo);
        return userMayBe.isPresent();
    }

    public boolean isAlreadyRegister(SignUpRequest request) {
        return existingContactNo(request.getPhoneNo());
    }

    /**
     * Fetches the user for the header context
     * This is yet to be implemented
     *
     * @return user
     */
    public User getLoggedInUser(){
        var retVal = new User();
        retVal.setId("default");
        return retVal;
    }

}
