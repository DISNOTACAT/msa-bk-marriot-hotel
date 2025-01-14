package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryRepository;
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
  public List<Inventory> increaseReserved(Reservation reservation) {

    inventoryRepository.increaseReserved(
        reservation.getHotelId(), RoomEntityType.fromDomain(reservation.getRoomType()),
        reservation.getStartDate(), reservation.getEndDate());

      return inventoryQueryDslRepository.findInventoryFromReservation(
          InventoryQuery.fromReservation(reservation)).stream()
          .map(RoomTypeInventoryEntity::toDomain).toList();
  }

  @Override
  public List<Inventory> decreaseReserved(Reservation reservation) {

    inventoryRepository.decreaseReserved(
        reservation.getHotelId(), RoomEntityType.fromDomain(reservation.getRoomType()),
        reservation.getStartDate(), reservation.getEndDate());

    return inventoryQueryDslRepository.findInventoryFromReservation(
            InventoryQuery.fromReservation(reservation)).stream()
        .map(RoomTypeInventoryEntity::toDomain).toList();
  }
}
