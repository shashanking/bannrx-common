package com.bannrx.common.test;

import com.bannrx.common.dtos.*;
import com.bannrx.common.enums.BusinessType;
import com.bannrx.common.enums.UserRole;
import com.bannrx.common.persistence.entities.Address;
import com.bannrx.common.persistence.entities.BankDetails;
import com.bannrx.common.persistence.entities.Business;
import com.bannrx.common.persistence.entities.User;
import com.bannrx.common.repository.AddressRepository;
import com.bannrx.common.repository.BankDetailsRepository;
import com.bannrx.common.repository.BusinessRepository;
import com.bannrx.common.repository.UserRepository;
import com.bannrx.common.service.AddressService;
import com.bannrx.common.service.BankDetailsService;
import com.bannrx.common.service.BusinessService;
import com.bannrx.common.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ObjectMapperUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BankDetailsRepository bankDetailsRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private BusinessRepository businessRepository;

    @Mock private AddressService addressService;
    @Mock private BankDetailsService bankDetailsService;
    @Mock private BusinessService businessService;
    @InjectMocks private UserService userService;

    private SignUpRequest validRequest;
    private User savedUser;
    private UserDto expectedUserDto;


    @BeforeEach
    public void setUp(){
        validRequest  =
                new SignUpRequest("Saurav", "1234567890", "saurav@gmail.com", "password@123", UserRole.ROLE_CAMPAIGNER,
                        Set.of(new AddressDto("AD12345","addressline1","addressline2","city","state","123456",1.25446536,2.152332)),
                        Set.of(new BankDetailsDto("BN12345",12345L,"PNB1245",true,"123456")),
                        new BusinessDto("BU12345","name", BusinessType.SERVICES)
        );

        savedUser = new User();
        savedUser.setId("UR20250322VIcPL65n24");
        savedUser.setEmail("saurav@gmail.com");

        expectedUserDto = new UserDto();
        expectedUserDto.setEmail(validRequest.getEmail());
    }

    @Test
    public void testCreateUser_Success() throws ServerException {

        when(bankDetailsService.toEntitySet(any(), any())).thenReturn(new HashSet<>());
        when(addressService.toEntitySet(any(), any())).thenReturn(new HashSet<>());
        when(businessService.toEntity(any())).thenReturn(new Business());
        when(userRepository.save(any())).thenReturn(savedUser);
        when(bankDetailsService.toDto(any())).thenReturn(new HashSet<>());
        when(addressService.toDto(any())).thenReturn(new HashSet<>());
        when(businessService.toDto(any())).thenReturn(new BusinessDto());

        UserDto result = userService.createUser(validRequest);

        assertNotNull(result);
        assertEquals(validRequest.getEmail(), result.getEmail());

    }

    @Test
    public void testToEntitySet() throws ServerException{

        Set<BankDetailsDto> bankDetailsDtoSet = Set.of(new BankDetailsDto("BN12345",12345L,"PNB1245",true,"123456"));
        Set<BankDetails> expectedBankDetails = new HashSet<>();
        BankDetails bankDetails = new BankDetails();
        bankDetails.setUser(savedUser);
        expectedBankDetails.add(bankDetails);

        when(bankDetailsService.toEntitySet(bankDetailsDtoSet, savedUser)).thenReturn(expectedBankDetails);
        Set<BankDetails> bankDetailsResult = bankDetailsService.toEntitySet(bankDetailsDtoSet, savedUser);

        assertEquals(1, bankDetailsResult.size());
        assertEquals(savedUser, bankDetailsResult.iterator().next().getUser());


        Set<AddressDto> addressDtoSet = Set.of(new AddressDto("AD12345","addressline1","addressline2","city","state","123456",1.25446536,2.152332));
        Set<Address> expectedAddress = new HashSet<>();
        Address address = new Address();
        address.setUser(savedUser);
        expectedAddress.add(address);

        when(addressService.toEntitySet(addressDtoSet, savedUser)).thenReturn(expectedAddress);

        Set<Address> addressResult = addressService.toEntitySet(addressDtoSet, savedUser);

        assertEquals(1, addressResult.size());
        assertEquals(savedUser, addressResult.iterator().next().getUser());
    }

    @Test
    public void testCreateUser_MissingRequiredFields() {
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setEmail("saurav@gmail.com");
        assertThrows(ServerException.class, () -> userService.createUser(invalidRequest));
    }

    @Test
    public void testCreateUser_InvalidEmailFormat() {
        validRequest.setEmail("invalid-email");
        assertThrows(ServerException.class, () -> userService.createUser(validRequest));
    }

    @Test
    public void testCreateUser_NullAddressSet() {
        validRequest.setAddressDtoSet(null);
        assertThrows(ServerException.class, () -> userService.createUser(validRequest));
    }

    @Test
    public void testCreateUser_EmptyBankDetailsSet() throws ServerException {
        validRequest.setBankDetailsDtoSet(Collections.emptySet());
        when(bankDetailsService.toEntitySet(any(), any())).thenReturn(Collections.emptySet());
        when(addressService.toEntitySet(any(), any())).thenReturn(new HashSet<>());
        when(businessService.toEntity(any())).thenReturn(new Business());
        when(userRepository.save(any())).thenReturn(savedUser);

        assertDoesNotThrow(() -> userService.createUser(validRequest));
    }

    @Test
    public void testCreateUser_DatabaseError() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> userService.createUser(validRequest));
    }

}
