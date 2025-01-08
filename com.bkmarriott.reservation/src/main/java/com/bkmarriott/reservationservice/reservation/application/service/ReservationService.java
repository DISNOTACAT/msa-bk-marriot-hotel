package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.exception.reservation.NoRoomAvailable;
import com.bkmarriott.reservationservice.reservation.application.exception.reservation.RoomSaveFailure;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationCommandOutputPort reservationCommandOutputPort;
  private final InventoryService inventoryService;

  @Transactional
  public Reservation createReservation(ReservationForCreate reservationForCreate) {

    // 해당 객실의 여유분 확인
    InventoryQueryResponseDto availableRoom = inventoryService.getInventoryQuantityByRoomType(
        reservationForCreate.getHotelId(),
        reservationForCreate.getStartDate(),
        reservationForCreate.getEndDate(),
        reservationForCreate.getRoomType()
    );
    log.info("[ReservationService] [createReservation] availableRoom ::: {}", availableRoom.getQuantity());


    if(availableRoom.getQuantity() == 0) {
      // 동시성 테스트 필요 (현재 비관적락 적용)
      throw new NoRoomAvailable("예약가능한 잔여 객실이 없습니다.");
    }

    // 결제 요청

    // 결제 응답 후, 객실 인벤토리 업데이트 요청
    // TODO: payment-service 생성 후 변경 필요
    ReservationStatus paymentResponse = ReservationStatus.PAID;
    if(paymentResponse == ReservationStatus.PAID) {
      reservationForCreate.setPaid();
      inventoryService.increasePaidReserved(reservationForCreate);
    }

    // 예약 응답
    try {
      Reservation reservation = reservationCommandOutputPort.save(
          reservationForCreate);
      log.info("[ReservationService] [createReservation] ::: 예약을 훌륭하게 성공했습니다. !!! ::: {}", reservation.getReservationId());
      return reservation;
    } catch (Exception e) {
      throw new RoomSaveFailure("객실 예약에 실패하였습니다.");
    }
  }
}
