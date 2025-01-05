package com.bkmarriott.charge.infrastructure.persistence.entity;

import com.bkmarriott.charge.domain.vo.RoomChargeId;
import com.bkmarriott.charge.domain.vo.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class RoomChargeEntityId implements Serializable {

    @Column(nullable = false)
    private Long hotelId;

    @Enumerated(EnumType.STRING)
    private RoomEntityType roomType;

    @Column(nullable = false)
    private LocalDate date;

    public RoomChargeEntityId(Long hotelId, RoomType roomType, LocalDate date) {
        this.hotelId = hotelId;
        this.roomType = RoomEntityType.fromDomain(roomType);
        this.date = date;
    }

    public RoomChargeId toDomain() {
        return RoomChargeId.of(hotelId, roomType.toDomain(), date);
    }

    public static RoomChargeEntityId fromDomain(RoomChargeId roomChargeId) {
        return new RoomChargeEntityId(
                roomChargeId.hotelId(),
                roomChargeId.roomType(),
                roomChargeId.date()
        );
    }
}
