package com.bannrx.common.service;

import com.bannrx.common.dtos.*;
import com.bannrx.common.enums.Status;
import com.bannrx.common.dtos.RegisterUser;
import com.bannrx.common.dtos.UserDto;
import com.bannrx.common.persistence.entities.User;
import com.bannrx.common.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@NoArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired private BusinessService businessService;
    @Autowired private BankDetailsService bankDetailsService;
    @Autowired private AddressService addressService;


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
            retVal.setCreatedBy(retVal.getEmail());
            retVal.setModifiedBy(retVal.getEmail());
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

    /**
     * Gets by id.
     *
     * @param id the id
     * @return Optional user
     */
    public Optional<User> getById(String id){
        return userRepository.findById(id);
    }

    public User fetchByPhoneNo(String phoneNo) throws InvalidInputException {
        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> new InvalidInputException(
                        String.format("User not found with phone no %s", phoneNo)
                ));
    }

    public User fetchByEmail(String email) throws InvalidInputException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidInputException(
                        String.format("User not found with email %s", email)
                ));
    }

    public boolean existingContactNo(String phoneNo){
        Optional<User> userMayBe = userRepository.findByPhoneNo(phoneNo);
        return userMayBe.isPresent();
    }

    public boolean existingEmail(String email){
        Optional<User> userMayBe = userRepository.findByEmail(email);
        return userMayBe.isPresent();
    }

    public boolean isAlreadyRegister(SignUpRequest request) {
        return (
                existingContactNo(request.getPhoneNo()) ||
                        existingEmail(request.getEmail())
        );
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userMayBe = getById(username);
        if (userMayBe.isPresent()){
            return userMayBe.get();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
