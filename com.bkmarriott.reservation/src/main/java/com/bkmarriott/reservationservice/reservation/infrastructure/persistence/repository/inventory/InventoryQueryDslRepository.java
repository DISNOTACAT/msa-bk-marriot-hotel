package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.inventory;

import static com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.QRoomTypeInventoryEntity.roomTypeInventoryEntity;

import com.bkmarriott.reservationservice.reservation.application.dto.AvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.application.dto.QAvailableRoomCountDto;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryDateQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomEntityType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.RoomTypeInventoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class InventoryQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<AvailableRoomCountDto> findAvailableRoomsByHotelIdAndDateRange(
      InventoryDateQuery query) {
    return queryFactory
        .select(
            new QAvailableRoomCountDto(
              roomTypeInventoryEntity.id.roomType,
              roomTypeInventoryEntity.totalInventory.subtract(roomTypeInventoryEntity.totalReserved).min()
            )
        )
        .from(roomTypeInventoryEntity)
        .where(roomTypeInventoryEntity.id.hotelId.eq(query.hotelId())
            .and(roomTypeInventoryEntity.id.date.between(query.startDate(), query.endDate())))
        .groupBy(roomTypeInventoryEntity.id.roomType)
        .fetch();
  }

    public List<RoomTypeInventoryEntity> findInventoryFromReservation(
        InventoryQuery query) {
        return queryFactory
                .selectFrom(roomTypeInventoryEntity)
                .where(
                        roomTypeInventoryEntity.id.hotelId.eq(query.hotelId())
                                .and(roomTypeInventoryEntity.id.date.between(query.startDate(), query.endDate()))
                                .and(roomTypeInventoryEntity.id.roomType.eq(
                                    RoomEntityType.fromDomain(query.roomType())))
                )
                .orderBy(
                        roomTypeInventoryEntity.totalInventory.subtract(roomTypeInventoryEntity.totalReserved).asc()
                )
                .fetch();
    }
}
