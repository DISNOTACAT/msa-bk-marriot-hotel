package com.bkmarriott.reservationservice.reservation.application.service.inventory;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomsWithChargeDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventorySearchRequestDto;
import com.bkmarriott.reservationservice.reservation.application.outputport.inventory.InventoryQueryOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.feign.ChargeOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryDateQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.inventory.response.InventorySearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryQueryService {

  private final InventoryQueryOutputPort inventoryQueryOutputPort;
  private final ChargeOutputPort chargeOutputPort;


  public List<InventorySearchResponse> getAvailableRoomsWithCharge(
      InventorySearchRequestDto searchDto) {

    List<AvailableRoomCountDto> availableRoomCount = inventoryQueryOutputPort
        .findAvailableRoomTypeAndCount(InventoryDateQuery.fromSearchDto(searchDto));

    if (availableRoomCount == null || availableRoomCount.isEmpty()) {
      log.warn("[InventoryService] [find Available Rooms] hotelId: {}, startDate: {}, endDate: {}", searchDto.getHotelId(), searchDto.getStartDate(), searchDto.getEndDate());
      throw new IllegalArgumentException("예약 가능 객실 수량 조회 결과가 없습니다.");
    }

    return availableRoomCount.stream()
        .map(roomCountDto -> setRoomCharge(searchDto, roomCountDto))
        .map(InventorySearchResponse::from)
        .toList();
  }

  private AvailableRoomsWithChargeDto setRoomCharge(InventorySearchRequestDto searchDto, AvailableRoomCountDto roomCountDto) {
          int charge = chargeOutputPort.findRoomChargeByDates(InventoryQuery.of(searchDto, roomCountDto.getRoomType()));
          return new AvailableRoomsWithChargeDto(roomCountDto.getRoomType(), roomCountDto.getCount(), charge);
  }
}
