package com.bkmarriott.charge.application.service;

import com.bkmarriott.charge.application.exception.RoomChargeDuplicatedException;
import com.bkmarriott.charge.application.exception.RoomChargeNotFoundException;
import com.bkmarriott.charge.application.outputport.HotelTypeOutputPort;
import com.bkmarriott.charge.application.outputport.RoomChargeOutputPort;
import com.bkmarriott.charge.domain.DefaultRoomCharge;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomChargeService {

    private final RoomChargeOutputPort roomChargeOutputPort;
    private final HotelTypeOutputPort hotelTypeOutputPort;

    @Transactional
    public RoomCharge create(RoomChargeForCreate roomChargeForCreate) {
        log.info("[RoomChargeService] [create] hotelId ::: {}, roomType ::: {}", roomChargeForCreate.id().hotelId(), roomChargeForCreate.id().roomType());

        roomChargeOutputPort.findById(roomChargeForCreate.id()).ifPresent(roomCharge -> {
            throw new RoomChargeDuplicatedException();
        });

        return roomChargeOutputPort.create(roomChargeForCreate);
    }

    public RoomCharge findOne(RoomChargeId roomChargeId) {
        log.info("[RoomChargeService] [findOne] hotelId ::: {}, roomType ::: {}", roomChargeId.hotelId(), roomChargeId.roomType());

        return roomChargeOutputPort.findById(roomChargeId)
                .orElseThrow(RoomChargeNotFoundException::new);
    }

    @Transactional
    public RoomCharge update(RoomChargeForCreate roomChargeForCreate) {
        log.info("[RoomChargeService] [update] hotelId ::: {}, roomType ::: {}", roomChargeForCreate.id().hotelId(), roomChargeForCreate.id().roomType());

        RoomCharge roomCharge = roomChargeOutputPort.findById(roomChargeForCreate.id())
                .orElseThrow(RoomChargeNotFoundException::new);

        return roomChargeOutputPort.updateCharge(roomCharge, roomChargeForCreate.charge());
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void createAllNextDefaultCharge() {
        LocalDate date = LocalDate.now().plusMonths(3);

        log.info("[RoomChargeService] [createAllNextDefaultCharge] date ::: {}", date);

        Map<RoomType, Integer> roomChargeMap = roomChargeOutputPort.findAllDefault().stream()
                .collect(Collectors.toMap(DefaultRoomCharge::getRoomType, DefaultRoomCharge::getCharge));

        List<RoomChargeForCreate> roomChargeForCreateList = hotelTypeOutputPort.findAll().stream()
                .map(hotelType -> RoomChargeForCreate.of(hotelType.getHotelId(), hotelType.getRoomType(), date, roomChargeMap.get(hotelType.getRoomType())))
                .toList();

        roomChargeOutputPort.bulkCreate(roomChargeForCreateList);
    }
}
