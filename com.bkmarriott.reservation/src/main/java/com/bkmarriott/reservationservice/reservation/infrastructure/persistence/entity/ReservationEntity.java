package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationForCreate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "m_reservation")
@Entity
public class ReservationEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "hotel_id", nullable = false)
  private Long hotelId;

  @Column(name = "room_id")
  private Long roomId;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "room_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private RoomEntityType roomType;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ReservationEntityStatus status;

  public Reservation toDomain() {
    return new Reservation(
        id,
        userId,
        hotelId,
        startDate,
        endDate,
        roomType.toDomain(),
        status.toDomain(),
        roomId
    );
  }

  public static ReservationEntity from(ReservationForCreate reservationForCreate){
    ReservationEntity reservation = new ReservationEntity(
            null,
            reservationForCreate.userId(),
            reservationForCreate.hotelId(),
            null,
            reservationForCreate.startDate(),
            reservationForCreate.endDate(),
            RoomEntityType.fromDomain(reservationForCreate.roomType()),
            ReservationEntityStatus.PENDING
    );
    reservation.createdBySystem(); //TODO 추후에 Auditing 자동처리
    return reservation;
  }
  public static ReservationEntity from(Reservation reservation){
    return new ReservationEntity(
            reservation.getReservationId(),
            reservation.getUserId(),
            reservation.getHotelId(),
            reservation.getRoomId(),
            reservation.getStartDate(),
            reservation.getEndDate(),
            RoomEntityType.fromDomain(reservation.getRoomType()),
            ReservationEntityStatus.fromDomain(reservation.getStatus())
    );
  }
  public ReservationEntity updateStatus(ReservationEntityStatus status){
    this.status = status;
    return this;
  }

}
