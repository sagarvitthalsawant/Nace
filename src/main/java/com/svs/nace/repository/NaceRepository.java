package com.svs.nace.repository;

import com.svs.nace.entity.EconomicActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaceRepository extends JpaRepository<EconomicActivity, Long> {

    EconomicActivity findByOrderNo(Long orderNo);
}
