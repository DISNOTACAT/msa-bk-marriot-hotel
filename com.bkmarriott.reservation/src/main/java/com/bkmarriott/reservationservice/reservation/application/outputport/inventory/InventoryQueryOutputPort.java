package com.bkmarriott.reservationservice.reservation.application.outputport.inventory;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryDateQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryQueryOutputPort {

  Optional<Inventory> findById(Long hotelId, LocalDate date, RoomType roomType);

  List<AvailableRoomCountDto> findAvailableRoomTypeAndCount(
      InventoryDateQuery query);

  List<Inventory> findInventoryFromReservation(InventoryQuery query);
}
