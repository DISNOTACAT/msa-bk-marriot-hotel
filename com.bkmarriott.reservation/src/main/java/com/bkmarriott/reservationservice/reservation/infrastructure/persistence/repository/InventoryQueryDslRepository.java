package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import static com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.QRoomTypeInventoryEntity.*;

import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.InventoryQuery.Response;
import com.bkmarriott.reservationservice.reservation.presentation.rest.dto.query.QInventoryQuery_Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InventoryQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<Response> findAvailableRoomsByHotelIdAndDateRange(Long hotelId, LocalDate startDate, LocalDate endDate) {
    return queryFactory
        .select(
            new QInventoryQuery_Response(
              roomTypeInventoryEntity.id.roomType,
              roomTypeInventoryEntity.totalInventory.subtract(roomTypeInventoryEntity.totalReserved).min()
            )
        )
        .from(roomTypeInventoryEntity)
        .where(roomTypeInventoryEntity.id.hotelId.eq(hotelId)
            .and(roomTypeInventoryEntity.id.date.between(startDate, endDate)))
        .groupBy(roomTypeInventoryEntity.id.roomType)
        .fetch();
  }


}
