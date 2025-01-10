package com.bkmarriott.charge.infrastructure.persistence.entity;

import com.bkmarriott.charge.domain.HotelType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(
        name = "m_hotel_type",
        uniqueConstraints = {@UniqueConstraint(columnNames = ("hotel_id, room_type"))}
)
@Entity
public class HotelTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hotelId;

    @Enumerated(EnumType.STRING)
    private RoomEntityType roomType;

    public HotelType toDomain() {
        return new HotelType(id, hotelId, roomType.toDomain());
    }
}
