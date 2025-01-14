package com.bkmarriott.reservationservice.reservation.domain;

import com.bkmarriott.reservationservice.reservation.application.exception.NoAvailableRoomsException;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Inventory {

  private Long hotelId;
  private LocalDate date;
  private RoomType roomType;

  private int totalInventory;
  private int totalReserved;
  
  private int version;

  public static Inventory of(Long hotelId, LocalDate date, RoomType roomType, int totalInventory,int totalReserved) {
    return Inventory.builder()
        .hotelId(hotelId)
        .date(date)
        .roomType(roomType)
        .totalInventory(totalInventory)
        .totalReserved(totalReserved)
        .build();
  }

  public static List<Inventory> of(Long hotelId, RoomType roomType, LocalDate startDate, LocalDate endDate) {
    return getDateRange(startDate, endDate).stream()
        .map(date -> Inventory.builder()
            .hotelId(hotelId)
            .date(date)
            .roomType(roomType)
            .build())
        .toList();
  }

  public static List<Inventory> from(Reservation reservation) {
    return getDateRange(reservation.getStartDate(), reservation.getEndDate()).stream()
        .map(date -> Inventory.builder()
            .hotelId(reservation.getHotelId())
            .date(date)
            .roomType(reservation.getRoomType())
            .build())
        .toList();
  }

  public static List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {

    return startDate.datesUntil(endDate.plusDays(1)).toList();
  }

  public int getAvailableRoomCount(){
    if(totalInventory <= totalReserved) throw new NoAvailableRoomsException("남은 방이 없습니다.");
    return totalInventory - totalReserved;
  }
}
