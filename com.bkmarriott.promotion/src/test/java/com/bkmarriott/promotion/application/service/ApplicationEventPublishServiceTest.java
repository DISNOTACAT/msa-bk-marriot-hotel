package com.bkmarriott.promotion.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bkmarriott.promotion.domain.event.DomainEventEnvelop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("[Application] [Unit] ApplicationEventPublishService Test")
@ExtendWith(MockitoExtension.class)
class ApplicationEventPublishServiceTest {

    @InjectMocks
    private ApplicationEventPublishService applicationEventPublishService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    @DisplayName("[성공] 이벤트 발행 테스트 - DomainEventEnvelop 객체를 이벤트객체로 변환후 발행")
    public void publish_successTest() {
        // Given
        DomainEventEnvelop envelop = Mockito.mock(DomainEventEnvelop.class);
        // When & Then
        Assertions.assertAll(
            () -> Assertions.assertDoesNotThrow(() -> applicationEventPublishService.publish(envelop))
        );
    }
}