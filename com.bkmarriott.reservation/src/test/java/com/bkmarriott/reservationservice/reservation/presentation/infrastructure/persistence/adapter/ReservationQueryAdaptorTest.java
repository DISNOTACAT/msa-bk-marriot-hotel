package com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.adapter;

import com.bkmarriott.reservationservice.reservation.domain.Reservation;
import com.bkmarriott.reservationservice.reservation.domain.vo.ReservationStatus;
import com.bkmarriott.reservationservice.reservation.domain.vo.RoomType;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.adapter.ReservationQueryAdaptor;
import com.bkmarriott.reservationservice.reservation.infrastructure.persistence.repository.ReservationRepository;
import com.bkmarriott.reservationservice.reservation.presentation.infrastructure.persistence.config.RepositoryTest;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("[Infrastructure] Inventory Repository Unit Test")
@RepositoryTest
class ReservationQueryAdaptorTest {

  @Autowired
  private ReservationRepository repository;
  @Autowired
  private ReservationQueryAdaptor reservationQueryAdaptor;


  @Test
  @DisplayName("[예약 조회 성공 테스트] 호텔 아이디를 조회한 후, 도메인 객체를 반환한다.")
  void find_successTest() {
    //Given
    Long reservationId = 1L;
    Reservation reservation = new Reservation(1L, 1L, 101L, LocalDate.of(2025,2,1), LocalDate.of(2025,2,2), RoomType.DELUXE, ReservationStatus.PAID, null);

    //When
    Optional<Reservation> actual = reservationQueryAdaptor.findById(reservationId);
    //Then
    Assertions.assertAll(
        () -> Assertions.assertEquals(reservationId, actual.get().getReservationId()),
        () -> Assertions.assertEquals(reservation.getHotelId(), actual.get().getHotelId()),
        () -> Assertions.assertEquals(reservation.getStartDate(), actual.get().getStartDate()),
        () -> Assertions.assertEquals(reservation.getEndDate(), actual.get().getEndDate()),
        () -> Assertions.assertEquals(reservation.getRoomType(), actual.get().getRoomType()),
        () -> Assertions.assertEquals(reservation.getStatus(), actual.get().getStatus())
    );
  }
}
