package com.bkmarriott.charge.infrastructure.persistence.repository;

import com.bkmarriott.charge.infrastructure.persistence.entity.HotelTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelTypeRepository extends JpaRepository<HotelTypeEntity, Long> {

    List<HotelTypeEntity> findAllByOrderByHotelIdAsc();
}
