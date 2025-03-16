package com.bannrx.common.service;

import com.bannrx.common.dtos.AddressDto;
import com.bannrx.common.enums.Status;
import com.bannrx.common.persistence.entities.Address;
import com.bannrx.common.persistence.entities.User;
import com.bannrx.common.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rklab.utility.annotations.Loggable;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ObjectMapperUtils;

import java.util.List;


@Service
@Loggable
@RequiredArgsConstructor
public class AddressService {

    private static final String DELETE_MSG = "Address(s) has been deleted";
    private final AddressRepository addressRepository;


    @Transactional
    public Address save(List<AddressDto> addressDtoList) throws ServerException {

        for (var addressDto : addressDtoList) {
            var retVal = ObjectMapperUtils.map(addressDto, Address.class);
            retVal.setStatus(Status.ACTIVE);
            return addressRepository.save(retVal);
        }
        throw new ServerException("You can add only one address");

    }

    public String delete(String id) throws ServerException {

        var addressMaybe = addressRepository.findById(id);
        addressRepository.delete(addressMaybe.get());
        return DELETE_MSG;
    }


    public void canAddressBeAdded(User user, AddressDto addressDto) throws InvalidInputException {
        /*if (user.getAddress() != null){
            throw new InvalidInputException("You can add only 1 addresses to add new address delete existing address.");
        }*/
    }


    /*public boolean isSameAddressExist(AddressDto addressDto, User user) {
       var address = user.getAddress();
        return addressDto.getAddressLine1().equalsIgnoreCase(address.getAddressLine1())
                && addressDto.isAddressLine2Equal(address.getAddressLine2())
                && addressDto.getPincode().equalsIgnoreCase(address.getPinCode())
                && addressDto.getCity().equalsIgnoreCase(address.getCity())
                && addressDto.getState().equalsIgnoreCase(address.getState());
    }*/

    public AddressDto update(AddressDto addressDto) throws InvalidInputException, ServerException {
        var address = fetchById(addressDto.getId());
        ObjectMapperUtils.map(addressDto, address);
        address = addressRepository.save(address);
        addressDto = ObjectMapperUtils.map(address, AddressDto.class);
        return addressDto;
    }

    public Address fetchById(String addressId) throws InvalidInputException {
        return addressRepository.findById(addressId).
                orElseThrow(()->new InvalidInputException("Address not found with id "+addressId));
    }
}
