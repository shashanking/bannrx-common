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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Loggable
@RequiredArgsConstructor
public class AddressService {

    private static final String DELETE_MSG = "Address(s) has been deleted";
    private final AddressRepository addressRepository;


    @Transactional
    public Set<Address> save(List<AddressDto> addressDtoList, User user) throws ServerException {
        Set<Address> addresses = new HashSet<>();

        for(var dto : addressDtoList){
            var address = ObjectMapperUtils.map(dto, Address.class);
            address.setStatus(Status.ACTIVE);
            address.setUser(user);
            address = addressRepository.save(address);
            addresses.add(address);
        }
        return addresses;
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

    public Set<Address> toEntitySet(Set<AddressDto> addressDtoSet, User user) throws ServerException {
        Set<Address> addressSet = new HashSet<>();
        for(var dto : addressDtoSet){
            var entity = ObjectMapperUtils.map(dto, Address.class);
            entity.setUser(user);
            addressSet.add(entity);
        }
        return addressSet;
    }

    public Set<AddressDto> toDto(Set<Address> addresses) throws ServerException {
        Set<AddressDto> addressDtoSet = new HashSet<>();
        for(var entity : addresses){
            entity = addressRepository.save(entity);
            var dto = ObjectMapperUtils.map(entity, AddressDto.class);
            addressDtoSet.add(dto);
        }
        return addressDtoSet;
    }
}
