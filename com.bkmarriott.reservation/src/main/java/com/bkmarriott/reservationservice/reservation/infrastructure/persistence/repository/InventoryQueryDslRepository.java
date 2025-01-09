package com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository;

import static com.bkmarriott.reservationservice.reservation.infrastructure.persistence.entity.QRoomTypeInventoryEntity.*;

import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryRequestDto;
import com.bkmarriott.reservationservice.reservation.application.dto.inventory.InventoryQueryResponseDto;
import com.bkmarriott.reservationservice.reservation.application.dto.inventory.QInventoryQueryResponseDto;
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
            .and(roomTypeInventoryEntity.id.date.between(requestDto.getStartDate(), requestDto.getEndDate().minusDays(1))))
        .groupBy(roomTypeInventoryEntity.id.roomType)
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .fetch();
  }

  public List<RoomTypeInventoryEntity> findAvailableRoomsWithPessimisticLock(
      InventoryQueryRequestDto requestDto) {
    return queryFactory
        .select(roomTypeInventoryEntity) // Entity 전체를 반환
        .from(roomTypeInventoryEntity)
        .where(
            roomTypeInventoryEntity.id.hotelId.eq(requestDto.getHotelId())
                .and(roomTypeInventoryEntity.id.date.between(
                    requestDto.getStartDate(),
                    requestDto.getEndDate().minusDays(1))
                )
        )
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .fetch();
  }
}
