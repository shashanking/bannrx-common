package com.bannrx.common.service;

import com.bannrx.common.dtos.*;
import com.bannrx.common.dtos.UserDto;
import com.bannrx.common.persistence.entities.Address;
import com.bannrx.common.persistence.entities.BankDetails;
import com.bannrx.common.persistence.entities.User;
import com.bannrx.common.repository.UserRepository;
import lombok.NoArgsConstructor;
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
import java.util.Set;



@Service
@Loggable
@NoArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepository;
    @Autowired private BusinessService businessService;
    @Autowired private BankDetailsService bankDetailsService;
    @Autowired private AddressService addressService;


    @Transactional
    public UserDto createUser(SignUpRequest request) throws ServerException {
        var user = ObjectMapperUtils.map(request, User.class);
        var bankDetails = bankDetailsService.toEntitySet(request.getBankDetailsDtoSet(),user);
        user.setBankDetails(bankDetails);
        var addresses = addressService.toEntitySet(request.getAddressDtoSet(), user);
        user.setAddresses(addresses);
        var business = businessService.toEntity(request.getBusinessDto());
        user.setBusiness(business);
        user = userRepository.save(user);
        var userDto = ObjectMapperUtils.map(user, UserDto.class);
        var bankDtoSet = bankDetailsService.toDto(user.getBankDetails());
        var addressDtoSet = addressService.toDto(user.getAddresses());
        var businessDto = businessService.toDto(user.getBusiness());
        user.setCreatedBy(user.getEmail());
        user.setModifiedBy(user.getEmail());
        userDto.setAddressDtoSet(addressDtoSet);
        userDto.setBankDetailsDtoSet(bankDtoSet);
        userDto.setBusinessDto(businessDto);
        return userDto;
    }

    private static List<BankDetailsDto> getBankDetailsDtoList(Set<BankDetails> savedBankDetails) {
        return savedBankDetails.stream()
                .map(bankDetail -> {
                    try {
                        return ObjectMapperUtils.map(bankDetail, BankDetailsDto.class);
                    } catch (ServerException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private static List<AddressDto> getAddressDtos(Set<Address> savedAddressDetails) {
        return savedAddressDetails.stream()
                .map(address -> {
                    try {
                        return ObjectMapperUtils.map(address, AddressDto.class);
                    } catch (ServerException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
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
