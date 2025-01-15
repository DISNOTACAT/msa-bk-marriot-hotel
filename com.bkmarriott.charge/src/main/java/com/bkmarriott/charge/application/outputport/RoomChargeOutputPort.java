package com.bkmarriott.charge.application.outputport;

import com.bkmarriott.charge.domain.DefaultRoomCharge;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;

import java.util.List;
import java.util.Optional;

public interface RoomChargeOutputPort {

    Optional<RoomCharge> findById(RoomChargeId roomChargeId);

    List<RoomCharge> findAll(List<RoomChargeId> roomChargeIdList);

    RoomCharge create(RoomChargeForCreate roomChargeForCreate);

    void bulkCreate(List<RoomChargeForCreate> roomChargeForCreateList);

    RoomCharge updateCharge(RoomCharge roomCharge, Integer charge);

    List<DefaultRoomCharge> findAllDefault();
}
