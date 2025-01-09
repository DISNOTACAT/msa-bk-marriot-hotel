package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.exception.inventory.InventoryUpdateFailureException;
import com.bkmarriott.reservationservice.reservation.application.exception.reservation.NoRoomAvailable;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.InventoryQuery.Response;
import com.bkmarriott.reservationservice.reservation.application.exception.ResourceNotFoundException;
import java.time.LocalDate;
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
  private final ReservationQueryOutputPort reservationQueryOutputPort;

  public List<Inventory> updateTotalReserved(Long reservationId) {

    Reservation reservation = reservationQueryOutputPort.findById(reservationId)
        .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 예약 정보"));

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

  public List<Response> getInventoryQuantity(Long hotelId, LocalDate startDate, LocalDate endDate) {

    List<InventoryQueryResponseDto> availableRooms = inventoryQueryOutputPort
        .findAvailableRoomsByHotelIdAndDateRange(new InventoryQueryRequestDto(
            hotelId,startDate,endDate
        ));

    if (availableRooms == null || availableRooms.isEmpty()) {
      log.warn("[InventoryService] [find Available Rooms] hotelId: {}, startDate: {}, endDate: {}", hotelId, startDate, endDate);
      throw new IllegalArgumentException("예약 가능 객실 수량 조회 결과가 없습니다.");
    }

    return availableRooms.stream().map(Response::from).toList();
  }

  // TODO: 예약결제 임시 메서드 추후 통합 또는 삭제 필요 -> 예약시 객실 조회 및 Lock
  public int findAvailableRoomsWithPessimisticLock(Long hotelId, LocalDate startDate, LocalDate endDate, RoomType roomType) {

    int availableRooms = inventoryCommandOutputPort
        .findAvailableRoomsWithPessimisticLock(new InventoryQueryRequestDto(
            hotelId,startDate,endDate, roomType
        ));

    if(availableRooms == 0) {
      throw new NoRoomAvailable("예약 가능한 잔여 객실이 없습니다.");
    }

    log.info("[InventoryService] [find Available Room By RoomType] hotelId: {}, startDate: {}, endDate: {}, roomType: {}", hotelId, startDate, endDate, roomType);
    return availableRooms;

  }

  public List<Inventory> increasePaidReserved(ReservationForCreate reservationForCreate) {

    try {

      if (reservationForCreate.getStatus().equals(ReservationStatus.PAID)) {
        log.debug("[InventoryService] [increasePaidReserved] hotelId ::: {}, roomtype ::: {}"
            , reservationForCreate.getHotelId(), reservationForCreate.getRoomType());

        return Inventory.of(
                reservationForCreate.getHotelId(),
                reservationForCreate.getStartDate(),
                reservationForCreate.getEndDate(),
                reservationForCreate.getRoomType()
            )
            .stream()
            .map(inventory -> inventoryCommandOutputPort.increaseReserved(inventory)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 인벤토리 정보")))
            .toList();
      }

      throw new IllegalArgumentException("잘못된 예약 상태 정보");

    } catch (Exception e) {
      log.error("[InventoryService] [updateTotalReserved] hotelId ::: {}, roomType ::: {}",
          reservationForCreate.getHotelId(), reservationForCreate.getRoomType(), e);
      throw new InventoryUpdateFailureException("객실 예약 인벤토리 정보 수정 실패");
    }
  }
}
