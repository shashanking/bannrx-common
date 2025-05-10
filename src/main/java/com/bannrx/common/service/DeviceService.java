package com.bannrx.common.service;

import com.bannrx.common.dtos.device.DeviceDto;
import com.bannrx.common.dtos.device.DeviceRegistration;
import com.bannrx.common.persistence.entities.Device;
import com.bannrx.common.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rklab.utility.annotations.Loggable;
import rklab.utility.expectations.InvalidInputException;
import rklab.utility.expectations.ServerException;
import rklab.utility.utilities.ObjectMapperUtils;



@Loggable
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceDto register(DeviceRegistration request) throws ServerException {
        var device = toEntity(request);
        device = deviceRepository.save(device);
        return toDto(device);
    }

    private DeviceDto toDto(Device device) throws ServerException {
        return ObjectMapperUtils.map(device, DeviceDto.class);
    }

    private Device toEntity(DeviceRegistration request) throws ServerException {
        return ObjectMapperUtils.map(request, Device.class);
    }

    public DeviceDto update(DeviceDto request) throws InvalidInputException, ServerException {
        var device = fetchById(request.getId());
        ObjectMapperUtils.map(request, device);
        device = deviceRepository.save(device);
        return toDto(device);
    }

    private Device fetchById(String id) throws InvalidInputException {
        return deviceRepository.findById(id)
                .orElseThrow(()-> new InvalidInputException(
                        String.format("This Id is not belongs to any device or invalid device Id %s", id))
                );
    }

    public void delete(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    public Boolean existById(String deviceId){
        return deviceRepository.existsById(deviceId);
    }

    public void validatePositiveNumber(Number value, String fieldName) throws InvalidInputException {
        if (value != null && value.doubleValue() <= 0) {
            throw new InvalidInputException(fieldName + " must be positive.");
        }
    }
}
