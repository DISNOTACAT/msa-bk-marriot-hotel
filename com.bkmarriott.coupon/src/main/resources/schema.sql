DROP TABLE IF EXISTS M_USER_COUPON;
DROP TABLE IF EXISTS M_COUPON;
DROP TABLE IF EXISTS M_COUPON_POLICY;

-- auto-generated definition
CREATE TABLE M_COUPON_POLICY
(
    ID         BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    STARTED_AT DATETIME(6)                      NULL,
    ENDED_AT   DATETIME(6)                      NULL,
    AFTER_DAY  INT                              NULL,
    TYPE       ENUM ('AFTER', 'FIXED', 'MIXED') NOT NULL,
    IS_DELETED      BIT                                          NOT NULL,
    CREATED_AT      DATETIME(6)                                  NOT NULL,
    CREATED_BY      BIGINT                                       NOT NULL,
    UPDATED_AT      DATETIME(6)                                  NOT NULL,
    UPDATED_BY      BIGINT                                       NOT NULL,
    DELETED_AT      DATETIME(6)                                  NULL,
    DELETED_BY      BIGINT                                       NULL
);


CREATE TABLE M_COUPON
(
    ID               BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    COUPON_POLICY_ID BIGINT       NOT NULL,
    DISCOUNT_RATE    FLOAT        NOT NULL,
    NAME             VARCHAR(255) NOT NULL,
    CONSTRAINT M_COUPON_NAME
        FOREIGN KEY (COUPON_POLICY_ID) REFERENCES M_COUPON_POLICY (ID),
    IS_DELETED      BIT                                          NOT NULL,
    CREATED_AT      DATETIME(6)                                  NOT NULL,
    CREATED_BY      BIGINT                                       NOT NULL,
    UPDATED_AT      DATETIME(6)                                  NOT NULL,
    UPDATED_BY      BIGINT                                       NOT NULL,
    DELETED_AT      DATETIME(6)                                  NULL,
    DELETED_BY      BIGINT                                       NULL
);

CREATE TABLE M_USER_COUPON
(
    ID          BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    USER_ID     BIGINT      NOT NULL,
    COUPON_ID   BIGINT      NOT NULL,
    CONSTRAINT M_USER_COUPON_COUPON_ID
        FOREIGN KEY (COUPON_ID) REFERENCES M_COUPON (ID),
    ISSUANCE_AT DATETIME(6) NOT NULL,
    SPENDING_AT DATETIME(6) NULL,
    EXPIRED_AT      DATETIME(6)                                  NOT NULL,
    IS_DELETED      BIT                                          NOT NULL,
    CREATED_AT      DATETIME(6)                                  NOT NULL,
    CREATED_BY      BIGINT                                       NOT NULL,
    UPDATED_AT      DATETIME(6)                                  NOT NULL,
    UPDATED_BY      BIGINT                                       NOT NULL,
    DELETED_AT      DATETIME(6)                                  NULL,
    DELETED_BY      BIGINT                                       NULL
);

