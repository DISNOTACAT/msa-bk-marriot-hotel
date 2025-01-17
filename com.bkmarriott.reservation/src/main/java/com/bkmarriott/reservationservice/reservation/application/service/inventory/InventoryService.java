package com.bkmarriott.reservationservice.reservation.application.service.inventory;

import com.bkmarriott.reservationservice.reservation.application.exception.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
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
  private final InventoryQueryOutputPort inventoryQueryOutputPort;
  private final InventoryCacheOutputPort inventoryCacheOutputPort;

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
    List<Inventory> inventoryFromReservation = inventoryQueryOutputPort.findInventoryFromReservation(query);
    inventoryFromReservation.stream()
            .mapToInt(Inventory::getAvailableRoomCount)
            .min()
            .orElseThrow(() -> new ResourceNotFoundException("해당 예약정보에 해당하는 객실 정보를 찾을 수 없습니다."));

    inventoryCacheOutputPort.decreaseRoomCount(query);
    // TODO DB 객실 선점 성공 히스토리 저장
  }
}
