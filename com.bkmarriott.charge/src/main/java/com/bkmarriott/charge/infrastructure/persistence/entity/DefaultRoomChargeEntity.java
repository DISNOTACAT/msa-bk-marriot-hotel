package com.bkmarriott.charge.infrastructure.persistence.entity;

import com.bkmarriott.charge.domain.DefaultRoomCharge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "m_default_room_charge")
@Entity
public class DefaultRoomChargeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoomEntityType roomType;

    @Column(nullable = false)
    private Integer charge;

    public DefaultRoomCharge toDomain() {
        return new DefaultRoomCharge(id, roomType.toDomain(), charge);
    }
}
