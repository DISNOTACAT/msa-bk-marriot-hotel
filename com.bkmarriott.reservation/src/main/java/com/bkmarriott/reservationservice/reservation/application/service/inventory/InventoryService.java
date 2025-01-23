package com.bkmarriott.reservationservice.reservation.application.service.inventory;

import com.bkmarriott.reservationservice.reservation.application.exception.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryMessageSender;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryCommandOutputPort inventoryCommandOutputPort;
  private final InventoryCacheOutputPort inventoryCacheOutputPort;
  private final InventoryMessageSender inventoryMessageSender;
  private final ReservationQueryOutputPort reservationQueryOutputPort;


  public List<Inventory> updateTotalReservedInventory(Long reservationId) {

    Reservation reservation = reservationQueryOutputPort.findById(reservationId)
        .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약 정보"));

    if (reservation.isPaid()) {
        log.debug("[InventoryService] [Increase totalReserved] hotelId ::: {}, roomtype ::: {}"
            , reservation.getHotelId(), reservation.getRoomType());

        return inventoryCommandOutputPort.increaseReservedInventory(reservation);
      }

    if (reservation.isFailedToPay()) {
      log.debug("[InventoryService] [Decrease totalReserved] hotelId ::: {}, roomtype ::: {}"
          , reservation.getHotelId(), reservation.getRoomType());

      return inventoryCommandOutputPort.decreaseReservedInventory(reservation);

    }

    log.error("[InventoryService] [updateTotalReserved] hotelId ::: {}, roomType ::: {}",
        reservation.getHotelId(), reservation.getRoomType());
    throw new InventoryUpdateFailureException("객실 예약 인벤토리 정보 수정 실패");

  }

  public void prepareAvailableRoom(InventoryQuery query) {
    log.info("[InventoryService] [prepareAvailableRoom] hotelId ::: {}, startDate ::: {}, endDate ::: {}, roomType ::: {}", query.hotelId(), query.startDate(), query.endDate(), query.roomType() );

    List<RoomInventoryEvent.RoomStockInfo> roomStockInfoList = inventoryCacheOutputPort.decreaseRoomCount(query);

    inventoryMessageSender.sendMessage(RoomInventoryEvent.prepare(roomStockInfoList));
  }
}
