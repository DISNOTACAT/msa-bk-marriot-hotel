package com.bkmarriott.promotion.application.listener;

import com.bkmarriott.promotion.application.event.CouponIssuance;
import com.bkmarriott.promotion.application.outputport.CouponExternalEventRecorder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponExternalEventRecordListener {

    private final CouponExternalEventRecorder eventRecorder;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void recordMessageHandle(CouponIssuance couponIssuance) {
        log.debug("[CouponExternalEventRecordListener] [recordMessageHandle] event ::: {}", couponIssuance);

        eventRecorder.record(couponIssuance.envelop());
    }
}
