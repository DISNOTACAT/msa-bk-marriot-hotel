package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.inventory;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryDateQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryId;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory.InventoryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryQueryAdaptor implements InventoryQueryOutputPort {

  private final InventoryRepository inventoryRepository;
  private final InventoryQueryDslRepository inventoryQueryDslRepository;

  @Override
  public Optional<Inventory> findById(Long hotelId, LocalDate date,
      RoomType roomType) {
    return inventoryRepository.findById(RoomTypeInventoryId.of(hotelId, date,roomType))
        .map(RoomTypeInventoryEntity::toDomain);
  }

  @Override
  public List<AvailableRoomCountDto> findAvailableRoomTypeAndCount(
      InventoryDateQuery query) {
    return inventoryQueryDslRepository.findAvailableRoomsByHotelIdAndDateRange(query);
  }
  @Override
  public List<Inventory> findInventoryFromReservation(InventoryQuery query) {
    return inventoryQueryDslRepository.findInventoryFromReservation(query).stream().map(RoomTypeInventoryEntity::toDomain).toList();
  }
}
