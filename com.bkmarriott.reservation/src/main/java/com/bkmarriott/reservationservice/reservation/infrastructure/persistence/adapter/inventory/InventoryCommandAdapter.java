package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.inventory;

import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class InventoryCommandAdapter implements InventoryCommandOutputPort {

  private final InventoryRepository inventoryRepository;
  private final InventoryQueryDslRepository inventoryQueryDslRepository;


  @Override
  public Optional<Inventory> increaseReserved(Inventory inventory) {
    return inventoryRepository.findById(RoomTypeInventoryId.of(
            inventory.getHotelId(), inventory.getDate(), inventory.getRoomType()))
        .map(RoomTypeInventoryEntity::increaseReserved)
        .map(RoomTypeInventoryEntity::toDomain);
  }

  @Override
  public Optional<Inventory> decreaseReserved(Inventory inventory) {
    return inventoryRepository.findById(RoomTypeInventoryId.of(
            inventory.getHotelId(), inventory.getDate(), inventory.getRoomType()))
        .map(RoomTypeInventoryEntity::decreaseReserved)
        .map(RoomTypeInventoryEntity::toDomain);
  }

  @Override
  public int findAvailableRoomsWithPessimisticLock(
      InventoryQueryRequestDto inventoryQueryRequestDto) {
    List<Inventory> list = inventoryQueryDslRepository.findAvailableRoomsWithPessimisticLock(inventoryQueryRequestDto)
        .stream().map(RoomTypeInventoryEntity::toDomain).toList();

    int min = Integer.MAX_VALUE;
    for(Inventory inventory : list) {
      min = Math.min(min,
          inventory.getTotalInventory() - inventory.getTotalReserved());
    }

    return min;
  }
}
