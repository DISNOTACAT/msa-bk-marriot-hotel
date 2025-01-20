package com.bkmarriott.promotion.presentation.rest.controller;

import com.bkmarriott.promotion.application.service.TimeAttackCouponIssuanceService;
import com.bkmarriott.promotion.domain.vo.TimeAttackCouponIssuance;
import com.bkmarriott.promotion.presentation.rest.dto.TimeAttackCouponRequest;
import com.bkmarriott.promotion.presentation.rest.dto.auth.Actor;
import com.bkmarriott.promotion.presentation.rest.util.auth.LoginActor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions/time-attack")
@RestController
public class TimeAttackCouponController {

    private final TimeAttackCouponIssuanceService timeAttackCouponIssuanceService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/issuance")
    public boolean timeAttackCouponIssuance(
        @RequestBody TimeAttackCouponRequest request,
        @LoginActor Actor actor
    ) {
        log.debug("[TimeAttackCouponController] [timeAttackCouponIssuance] promotionId ::: {}, requestUser ::: {}", request.getPromotionId(), actor.userId());

        TimeAttackCouponIssuance promotion = request.toDomain(actor.userId());
        return timeAttackCouponIssuanceService.issue(promotion);
    }
}
