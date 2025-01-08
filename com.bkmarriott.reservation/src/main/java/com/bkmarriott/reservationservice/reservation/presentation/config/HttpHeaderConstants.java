package com.bkmarriott.reservationservice.reservation.presentation.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeaderConstants {

  public static final String HEADER_USER_ID = "X-User-Id";
  public static final String HEADER_ROLE = "X-Role";
}
