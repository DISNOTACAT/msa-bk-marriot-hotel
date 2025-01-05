package com.bkmarriott.charge.infrastructure.persistence.entity;

import com.bkmarriott.charge.domain.vo.RoomType;

public enum RoomEntityType {

    STANDARD, TWIN, DELUXE, SUITE;

    public RoomType toDomain() {
        return RoomType.valueOf(this.name());
    }

    public static RoomEntityType fromDomain(RoomType roomType) {
        return RoomEntityType.valueOf(roomType.name());
    }
}
