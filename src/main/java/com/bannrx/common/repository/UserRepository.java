package com.bannrx.common.repository;

import com.bannrx.common.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findByPhoneNo(String phoneNo);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrPhoneNo(final String email, final String phoneNo);

    boolean existsByEmailOrPhoneNo(String email, String phoneNo);
}
