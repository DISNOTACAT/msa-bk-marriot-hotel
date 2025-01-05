package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class InventoryCommandAdaptor implements InventoryCommandOutputPort {

  private final InventoryQueryAdaptor inventoryQueryAdaptor;

  @Override
  public Optional<Inventory> increaseReserved(Inventory inventory) {
    return inventoryQueryAdaptor.findById(inventory.getHotelId(), inventory.getDate(), inventory.getRoomType())
        .map(RoomTypeInventoryEntity::increaseReserved)
        .map(RoomTypeInventoryEntity::toDomain);
  }

  @Override
  public Optional<Inventory> decreaseReserved(Inventory inventory) {
    return inventoryQueryAdaptor.findById(inventory.getHotelId(), inventory.getDate(), inventory.getRoomType())
        .map(RoomTypeInventoryEntity::decreaseReserved)
        .map(RoomTypeInventoryEntity::toDomain);
  }
}
