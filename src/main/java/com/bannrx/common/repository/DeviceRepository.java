package com.bannrx.common.repository;
import com.bannrx.common.persistence.entities.Device;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface DeviceRepository extends JpaRepository<Device, String>,
        JpaSpecificationExecutor<Device> {
}
