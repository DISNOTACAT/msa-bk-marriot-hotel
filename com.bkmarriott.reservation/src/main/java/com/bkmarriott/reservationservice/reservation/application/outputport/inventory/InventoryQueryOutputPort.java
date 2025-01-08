package com.bkmarriott.reservationservice.reservation.application.outputport.inventory;

import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;

public interface InventoryQueryOutputPort {

  Optional<Inventory> findById(Long hotelId, LocalDate date, RoomType roomType);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<InventoryQueryResponseDto> findAvailableRoomsByHotelIdAndDateRange(
      InventoryQueryRequestDto inventoryQueryRequestDto);
}
