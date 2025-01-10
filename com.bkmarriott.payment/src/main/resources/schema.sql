DROP TABLE IF EXISTS M_PAYMENT;

CREATE TABLE M_PAYMENT
(
    ID               BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    RESERVATION_ID   BIGINT                                                        NOT NULL,
    ORIGINAL_PRICE   BIGINT                                                           NOT NULL,
    FINAL_PRICE      BIGINT                                                           NOT NULL,
    MEMBER_COUPON_ID BIGINT                                                        NULL,
    TRANSACTION_ID   VARCHAR(255)                                                  NULL,
    PAYMENT_STATUS   ENUM ('CANCELLED', 'PAID', 'PENDING', 'REFUNDED', 'REJECTED') NOT NULL,
    PAYMENT_TYPE     ENUM ('CARD', 'CASH')                                         NOT NULL,
    IS_DELETED       BIT                                                           NOT NULL,
    CREATED_AT       DATETIME(6)                                                   NOT NULL,
    CREATED_BY       BIGINT                                                        NOT NULL,
    UPDATED_AT       DATETIME(6)                                                   NOT NULL,
    UPDATED_BY       BIGINT                                                        NOT NULL,
    DELETED_AT       DATETIME(6)                                                   NULL,
    DELETED_BY       BIGINT                                                        NULL
);


