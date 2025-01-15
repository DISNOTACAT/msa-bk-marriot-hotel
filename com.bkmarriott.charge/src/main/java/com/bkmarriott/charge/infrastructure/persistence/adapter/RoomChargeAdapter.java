package com.bkmarriott.charge.infrastructure.persistence.adapter;

import com.bkmarriott.charge.application.outputport.RoomChargeOutputPort;
import com.bkmarriott.charge.domain.DefaultRoomCharge;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.infrastructure.persistence.entity.DefaultRoomChargeEntity;
import com.bkmarriott.charge.infrastructure.persistence.entity.RoomChargeEntity;
import com.bkmarriott.charge.infrastructure.persistence.entity.RoomChargeEntityId;
import com.bkmarriott.charge.infrastructure.persistence.repository.DefaultRoomChargeRepository;
import com.bkmarriott.charge.infrastructure.persistence.repository.RoomChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RoomChargeAdapter implements RoomChargeOutputPort {

    private final RoomChargeRepository roomChargeRepository;
    private final DefaultRoomChargeRepository defaultRoomChargeRepository;

    public Optional<RoomCharge> findById(RoomChargeId roomChargeId) {
        return roomChargeRepository.findByIdAndIsDeletedFalse(RoomChargeEntityId.fromDomain(roomChargeId))
                .map(RoomChargeEntity::toDomain);
    }

    public List<RoomCharge> findAll(List<RoomChargeId> roomChargeIdList) {
        return roomChargeRepository.findAllByIdIsInAndIsDeletedFalse(roomChargeIdList.stream().map(RoomChargeEntityId::fromDomain).toList())
                .stream()
                .map(RoomChargeEntity::toDomain)
                .toList();
    }

    public RoomCharge create(RoomChargeForCreate roomChargeForCreate) {
        RoomChargeEntity roomChargeEntity = RoomChargeEntity.from(roomChargeForCreate);
        return roomChargeRepository.save(roomChargeEntity).toDomain();
    }

    public void bulkCreate(List<RoomChargeForCreate> roomChargeForCreateList) {
        List<RoomChargeEntity> roomChargeEntities = roomChargeForCreateList.stream()
                .map(RoomChargeEntity::from)
                .toList();
        roomChargeRepository.saveAll(roomChargeEntities);
    }

    public RoomCharge updateCharge(RoomCharge roomCharge, Integer charge) {
        roomCharge.updateCharge(charge);
        RoomChargeEntity roomChargeEntity = RoomChargeEntity.fromDomain(roomCharge);
        return roomChargeRepository.save(roomChargeEntity).toDomain();
    }

    public List<DefaultRoomCharge> findAllDefault() {
        return defaultRoomChargeRepository.findAll().stream()
                .map(DefaultRoomChargeEntity::toDomain)
                .toList();
    }
}
