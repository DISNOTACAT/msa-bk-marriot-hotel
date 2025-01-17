package com.bkmarriott.reservationservice.reservation.application.outputport.inventory;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import java.util.List;

public interface InventoryCommandOutputPort {

  List<Inventory> increaseReservedInventory(Reservation reservation);

  List<Inventory> decreaseReservedInventory(Reservation reservation);
}
