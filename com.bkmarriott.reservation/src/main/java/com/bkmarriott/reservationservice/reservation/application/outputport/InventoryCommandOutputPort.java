package com.bkmarriott.reservationservice.reservation.application.outputport;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import java.util.List;

public interface InventoryCommandOutputPort {

  List<Inventory> increaseReserved(Reservation resrvation);

  List<Inventory> decreaseReserved(Reservation resrvation);
}
