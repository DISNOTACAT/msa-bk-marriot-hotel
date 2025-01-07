package com.bkmarriott.promotion.domain.vo;

import java.time.LocalDateTime;

public class PromotionPeriod {

    private final LocalDateTime startedAt;
    private final LocalDateTime endedAt;

    private PromotionPeriod(LocalDateTime startedAt, LocalDateTime endedAt) {
        validatePeriod(startedAt, endedAt);
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    private void validatePeriod(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (startedAt.isAfter(endedAt)) {
            throw new IllegalArgumentException("시작 날짜는 종료일자보다 이후일 수 없습니다.");
        }
    }

    public static PromotionPeriod valueOf(LocalDateTime startedAt, LocalDateTime endedAt) {
        return new PromotionPeriod(startedAt, endedAt);
    }

    public boolean isContains(LocalDateTime dateTime) {
        return startedAt.isBefore(dateTime) && endedAt.isAfter(dateTime);
    }
}
