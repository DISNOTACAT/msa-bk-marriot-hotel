package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.inventory;

import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class InventoryCommandAdaptor implements InventoryCommandOutputPort {

  private final InventoryRepository inventoryRepository;
  private final InventoryQueryDslRepository inventoryQueryDslRepository;

  @Override
  public List<Inventory> increaseReservedInventory(Reservation reservation) {

    inventoryRepository.increaseReserved(reservation);

    return getInventoryListByReservation(reservation);
  }

  @Override
  public List<Inventory> decreaseReservedInventory(Reservation reservation) {

    inventoryRepository.decreaseReserved(reservation);

    return getInventoryListByReservation(reservation);
  }

  private List<Inventory> getInventoryListByReservation(Reservation reservation) {
    return inventoryQueryDslRepository.findInventoryFromReservation(
            InventoryQuery.fromReservation(reservation)).stream()
        .map(RoomTypeInventoryEntity::toDomain).toList();
  }
}
