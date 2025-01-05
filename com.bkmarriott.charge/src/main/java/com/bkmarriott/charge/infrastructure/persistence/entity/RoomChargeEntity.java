package com.bkmarriott.charge.infrastructure.persistence.entity;

import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.domain.vo.RoomChargeForCreate;
import com.bkmarriott.charge.domain.vo.RoomChargeId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "m_room_charge")
@Entity
public class RoomChargeEntity extends BaseEntity {

    @EmbeddedId
    private RoomChargeEntityId id;

    @Column(nullable = false)
    private Integer charge;

    public RoomChargeEntity(RoomChargeId id, Integer charge) {
        this.id = RoomChargeEntityId.fromDomain(id);
        this.charge = charge;
    }

    public RoomCharge toDomain() {
        return new RoomCharge(id.toDomain(), charge);
    }

    public static RoomChargeEntity fromDomain(RoomCharge roomCharge) {
        return new RoomChargeEntity(
                roomCharge.getId(),
                roomCharge.getCharge()
        );
    }

    public static RoomChargeEntity from(RoomChargeForCreate roomChargeForCreate) {
        return new RoomChargeEntity(
                roomChargeForCreate.id(),
                roomChargeForCreate.charge()
        );
    }
}
