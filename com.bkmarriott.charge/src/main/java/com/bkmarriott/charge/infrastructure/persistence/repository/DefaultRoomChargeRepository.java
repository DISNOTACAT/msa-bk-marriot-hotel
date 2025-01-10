package com.bkmarriott.charge.infrastructure.persistence.repository;

import com.bkmarriott.charge.infrastructure.persistence.entity.DefaultRoomChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultRoomChargeRepository extends JpaRepository<DefaultRoomChargeEntity, Long> {
}
