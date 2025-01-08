package com.bkmarriott.promotion.infrastructure.cache.adapter;

import com.bkmarriott.promotion.application.outputport.TimeAttackCouponOutputPort;
import com.bkmarriott.promotion.domain.vo.CouponIssuanceResult;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import com.bkmarriott.promotion.infrastructure.cache.operation.RedisTransaction;
import com.bkmarriott.promotion.infrastructure.cache.operation.TimeAttackOperation;
import com.bkmarriott.promotion.infrastructure.cache.vo.TimeAttackVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimeAttackCouponCacheAdapter implements TimeAttackCouponOutputPort {

    private static final Long ADD_FAILURE_VALUE = 0L;

    private final RedisTransaction redisTransaction;
    private final TimeAttackOperation timeAttackOperation;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public CouponIssuanceResult tryIssuance(TimeAttackCouponIssuance issuance) {
        log.debug("[TimeAttackCouponCacheAdapter] [tryIssuance] requestUserId ::: {}, targetPromotion ::: {}, requestTime ::: {}", issuance.getRequestUserId(), issuance.getTargetPromotionId(), issuance.getRequestTime());

        TimeAttackVO timeAttackVO = TimeAttackVO.from(issuance);
        List result = redisTransaction.execute(redisTemplate, timeAttackOperation, timeAttackVO);
        return mapToResult(result);
    }

    private CouponIssuanceResult mapToResult(List<Object> result) {
        Long issuanceCount = (Long) result.get(0);
        Long addResult = (Long) result.get(1);
        boolean isDuplicated = ADD_FAILURE_VALUE.equals(addResult);

        return CouponIssuanceResult.valueOf(issuanceCount, isDuplicated);
    }
}
