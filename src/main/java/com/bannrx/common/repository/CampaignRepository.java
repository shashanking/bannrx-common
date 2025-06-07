package com.bannrx.common.repository;

import com.bannrx.common.persistence.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface CampaignRepository extends JpaRepository<Campaign, String>,
        JpaSpecificationExecutor<Campaign> {
}
