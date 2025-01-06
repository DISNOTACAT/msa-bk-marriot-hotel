package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import static com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.QRoomTypeInventoryEntity.*;

import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.dto.QInventoryQueryResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InventoryQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  public List<InventoryQueryResponseDto> findAvailableRoomsByHotelIdAndDateRange(
      InventoryQueryRequestDto requestDto) {
    return queryFactory
        .select(
            new QInventoryQueryResponseDto(
              roomTypeInventoryEntity.id.roomType,
              roomTypeInventoryEntity.totalInventory.subtract(roomTypeInventoryEntity.totalReserved).min()
            )
        )
        .from(roomTypeInventoryEntity)
        .where(roomTypeInventoryEntity.id.hotelId.eq(requestDto.getHotelId())
            .and(roomTypeInventoryEntity.id.date.between(requestDto.getStartDate(), requestDto.getEndDate())))
        .groupBy(roomTypeInventoryEntity.id.roomType)
        .fetch();
  }


}
