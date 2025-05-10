package com.bannrx.common.repository;
import com.bannrx.common.persistence.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;



public interface DeviceRepository extends JpaRepository<Device, String> {
}
