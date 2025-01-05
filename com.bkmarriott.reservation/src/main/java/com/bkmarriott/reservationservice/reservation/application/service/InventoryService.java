package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.exception.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.outputport.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.ReservationQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.InventoryQueryDslRepository;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Request;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Response;
import com.bkmarriott.reservationservice.reservation.presentation.rest.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryCommandOutputPort inventoryCommandOutputPort;
  private final ReservationQueryAdaptor reservationQueryAdaptor;
  private final InventoryQueryDslRepository inventoryQueryDslRepository;

  public List<Inventory> updateTotalReserved(Long reservationId) {

    Reservation reservation = reservationQueryAdaptor.findById(reservationId).toDomain();

    try {

      if (reservation.getStatus().equals(ReservationStatus.PAID)) {
          log.debug("[InventoryService] [Increase totalReserved] hotelId ::: {}, roomtype ::: {}"
              , reservation.getHotelId(), reservation.getRoomType());

          return Inventory.from(reservation)
              .stream()
              .map(inventory -> inventoryCommandOutputPort.increaseReserved(inventory)
                  .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 인벤토리 정보")))
              .toList();
        }

      if (reservation.getStatus().equals(ReservationStatus.CANCELLED) || reservation.getStatus().equals(ReservationStatus.REFUNDED)) {
        log.debug("[InventoryService] [Decrease totalReserved] hotelId ::: {}, roomtype ::: {}"
            , reservation.getHotelId(), reservation.getRoomType());

        return Inventory.from(reservation)
            .stream()
            .map(inventory -> inventoryCommandOutputPort.decreaseReserved(inventory)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 인벤토리 정보")))
            .toList();
        }

      throw new IllegalArgumentException("잘못된 예약 상태 정보");

    } catch (Exception e) {
      log.error("[InventoryService] [updateTotalReserved] hotelId ::: {}, roomType ::: {}",
          reservation.getHotelId(), reservation.getRoomType(), e);
      throw new InventoryUpdateFailureException("객실 예약 인벤토리 정보 수정 실패");
    }

  }

  public List<Response> getInventoryQuantity(Request request) {

    try {
      return inventoryQueryDslRepository.findAvailableRoomsByHotelIdAndDateRange(
          request.getHotelId()
          , request.getStartDate()
          , request.getEndDate());
    } catch (Exception e) {
      log.error("[InventoryService] [getInventoryQuantity] hotelId ::: {}, startDate ::: {}, endDate ::: {}",
          request.getHotelId(), request.getStartDate(), request.getEndDate(), e);
      throw new IllegalArgumentException("예약 가능 객실 수량 조회 실패");
    }
  }
}
