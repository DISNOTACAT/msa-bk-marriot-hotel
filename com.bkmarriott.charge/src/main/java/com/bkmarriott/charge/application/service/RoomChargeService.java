package com.bkmarriott.charge.application.service;

import com.bkmarriott.charge.application.exception.RoomChargeDuplicatedException;
import com.bkmarriott.charge.application.exception.RoomChargeNotFoundException;
import com.bkmarriott.charge.application.outputport.RoomChargeOutputPort;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomChargeService {

    private final RoomChargeOutputPort roomChargeOutputPort;

    @Transactional
    public RoomCharge create(RoomChargeForCreate roomChargeForCreate) {
        log.debug("[RoomChargeService] [create] hotelId ::: {}, roomType ::: {}", roomChargeForCreate.id().hotelId(), roomChargeForCreate.id().roomType());

        roomChargeOutputPort.findById(roomChargeForCreate.id()).ifPresent(roomCharge -> {
            throw new RoomChargeDuplicatedException();
        });

        return roomChargeOutputPort.create(roomChargeForCreate);
    }

    public RoomCharge findOne(RoomChargeId roomChargeId) {
        log.debug("[RoomChargeService] [findOne] hotelId ::: {}, roomType ::: {}", roomChargeId.hotelId(), roomChargeId.roomType());

        return roomChargeOutputPort.findById(roomChargeId)
                .orElseThrow(RoomChargeNotFoundException::new);
    }

    @Transactional
    public RoomCharge update(RoomChargeForCreate roomChargeForCreate) {
        log.debug("[RoomChargeService] [update] hotelId ::: {}, roomType ::: {}", roomChargeForCreate.id().hotelId(), roomChargeForCreate.id().roomType());

        RoomCharge roomCharge = roomChargeOutputPort.findById(roomChargeForCreate.id())
                .orElseThrow(RoomChargeNotFoundException::new);

        return roomChargeOutputPort.updateCharge(roomCharge, roomChargeForCreate.charge());
    }
}
