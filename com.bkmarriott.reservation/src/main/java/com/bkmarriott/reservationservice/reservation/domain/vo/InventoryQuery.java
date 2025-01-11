package com.bkmarriott.reservationservice.reservation.domain.vo;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public record InventoryQuery(
        Long hotelId,
        LocalDate startDate,
        LocalDate endDate,
        RoomType roomType
) {
    public static InventoryQuery fromReservationForCreate(ReservationForCreate reservationForCreate) {
        return new InventoryQuery(reservationForCreate.hotelId(), reservationForCreate.startDate(), reservationForCreate.endDate(), reservationForCreate.roomType());
    }

    public static InventoryQuery fromReservation(Reservation reservation) {
        return new InventoryQuery(reservation.getHotelId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getRoomType());
    }

    public List<LocalDate> getDateRange(){
        return startDate.datesUntil(endDate.plusDays(1)).toList();
    }
}
