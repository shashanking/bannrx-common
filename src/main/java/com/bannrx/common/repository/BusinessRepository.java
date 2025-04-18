package com.bannrx.common.repository;

import com.bannrx.common.persistence.entities.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessRepository extends JpaRepository<Business, String> {
}
