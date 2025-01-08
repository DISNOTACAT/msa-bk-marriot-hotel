package com.bkmarriott.reservationservice.reservation.application.outputport.inventory;

import com.bkmarriott.reservationservice.reservation.domain.Inventory;
import java.util.Optional;

public interface InventoryCommandOutputPort {

  Optional<Inventory> increaseReserved(Inventory inventory);

  Optional<Inventory> decreaseReserved(Inventory inventory);
}
