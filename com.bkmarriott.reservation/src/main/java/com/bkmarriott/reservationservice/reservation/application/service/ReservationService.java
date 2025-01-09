package com.bkmarriott.reservationservice.reservation.application.service;

import com.bkmarriott.reservationservice.reservation.application.exception.reservation.RoomSaveFailure;
import com.bkmarriott.reservationservice.reservation.application.outputport.CouponOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.PaymentOutputPort;
import com.bkmarriott.reservationservice.reservation.application.outputport.reservation.ReservationCommandOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.reservation.ReservationForCreate;
import com.bkmarriott.reservationservice.reservation.infrastructure.feignClient.dto.payment.CreatePayment;
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
  private final CouponOutputPort couponOutputPort;
  private final PaymentOutputPort paymentOutputPort;

  @Transactional
  public Reservation createReservation(ReservationForCreate reservationForCreate) {

    // 해당 객실의 여유분 확인
    int availableRoom = inventoryService.findAvailableRoomsWithPessimisticLock(
        reservationForCreate.getHotelId(),
        reservationForCreate.getStartDate(),
        reservationForCreate.getEndDate(),
        reservationForCreate.getRoomType()
    );
    log.info("[ReservationService] [createReservation] availableRoom ::: {}", availableRoom);

    // 쿠폰 할인가 적용
    if(reservationForCreate.getMemberCouponId() != null) {
      Float discountRate = couponOutputPort.getCouponDiscountRate(reservationForCreate.getMemberCouponId());
      log.info("[ReservationService] [GetMemberCouponResponse] coupon discountRate ::: {}", discountRate);
      reservationForCreate.discoundOriginPriceByCoupon(discountRate);

      // TODO: Feign 쿠폰 사용 요청
    }

    // 결제 정보 저장
    Long paymentId = paymentOutputPort.createPayment(CreatePayment.Requset.from(reservationForCreate));
    log.info("[ReservationService] [createPayment] paymentId ::: {}", paymentId);

    // 결제 후 객실 잔여 수량 수정
    reservationForCreate.setPaid();
    inventoryService.increasePaidReserved(reservationForCreate);

    // 예약 응답
    try {
      Reservation reservation = reservationCommandOutputPort.save(
          reservationForCreate);
      log.info("[ReservationService] [createReservation] reservationId ::: {}", reservation.getReservationId());
      paymentOutputPort.updateReservationId(paymentId, reservation.getReservationId());
      return reservation;
    } catch (Exception e) {
      throw new RoomSaveFailure("객실 예약에 실패하였습니다.");
    }
  }
}
