package com.bannrx.common.repository;

import com.bannrx.common.persistence.entities.BankDetails;
import com.bannrx.common.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, String> {
}
