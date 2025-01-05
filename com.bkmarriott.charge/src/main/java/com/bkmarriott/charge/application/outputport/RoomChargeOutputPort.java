package com.bkmarriott.charge.application.outputport;

import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;

import java.util.Optional;

public interface RoomChargeOutputPort {

    Optional<RoomCharge> findById(RoomChargeId roomChargeId);

    RoomCharge create(RoomChargeForCreate roomChargeForCreate);

    RoomCharge updateCharge(RoomCharge roomCharge, Integer charge);
}
