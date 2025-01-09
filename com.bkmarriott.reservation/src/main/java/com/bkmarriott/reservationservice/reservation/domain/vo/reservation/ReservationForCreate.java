package com.bkmarriott.reservationservice.reservation.domain.vo.reservation;

import com.bkmarriott.reservationservice.reservation.domain.vo.inventory.RoomType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationForCreate {

  private Long userId;
  private Long hotelId;
  private LocalDate startDate;
  private LocalDate endDate;
  private RoomType roomType;
  private ReservationStatus status;
  private Long memberCouponId;
  private int originPrice;
  private int finalPrice;

  public ReservationForCreate(Long userId, Long hotelId, LocalDate startDate,
      LocalDate endDate, RoomType roomType, Long memberCouponId, int price) {
    this.userId = userId;
    this.hotelId = hotelId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.roomType = roomType;
    this.status = ReservationStatus.PENDING;
    this.memberCouponId = memberCouponId;
    this.originPrice = price;
    this.finalPrice = price;
  }

  public void setPaid() {
    this.status = ReservationStatus.PAID;
  }

  public void discoundOriginPriceByCoupon(Float discountRate) {
    this.finalPrice = Math.round(originPrice * (1 - discountRate));
  }
}
