package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryQueryAdaptor implements InventoryQueryOutputPort {

  private final InventoryRepository inventoryRepository;

  @Override
  public Optional<Inventory> findById(Long hotelId, LocalDate date,
      RoomType roomType) {
    return inventoryRepository.findById(RoomTypeInventoryId.of(hotelId, date,roomType))
        .map(RoomTypeInventoryEntity::toDomain);
  }
}
