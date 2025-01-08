CREATE TABLE m_promotion
(
    promotion_id BIGINT       NOT NULL AUTO_INCREMENT,
    coupon_id    BIGINT       NOT NULL,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    started_at   TIMESTAMP    NOT NULL,
    ended_at     TIMESTAMP    NOT NULL,
    max_issuance INTEGER      NOT NULL,

    is_deleted   bit          not null,
    created_at   datetime(6)  not null,
    created_by   bigint       not null,
    updated_at   datetime(6)  not null,
    updated_by   bigint       not null,
    deleted_at   datetime(6),
    deleted_by   bigint,

    PRIMARY KEY (promotion_id)
);

CREATE TABLE m_coupon_issuance_outbox (
    outbox_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type      VARCHAR(100) NOT NULL,
    payload         TEXT NOT NULL,
    source          VARCHAR(100) NOT NULL,
    uuid            CHAR(36) NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    is_published    BOOLEAN NOT NULL DEFAULT FALSE
);