INSERT INTO m_promotion (
    coupon_id,
    name,
    description,
    started_at,
    ended_at,
    max_issuance,
    is_deleted,
    created_at,
    created_by,
    updated_at,
    updated_by,
    deleted_at,
    deleted_by
)
VALUES
    (1001, 'Summer Sale', 'Discounts on summer items', '2025-06-01 00:00:00', '2025-06-30 23:59:59', 1000, 0, NOW(), 1, NOW(), 1, NULL, NULL),
    (1002, 'Winter Fest', 'Exclusive winter deals', '2025-12-01 00:00:00', '2025-12-31 23:59:59', 500, 0, NOW(), 1, NOW(), 1, NULL, NULL),
    (1003, 'Black Friday', 'Biggest sale of the year', '2025-11-25 00:00:00', '2025-11-30 23:59:59', 2000, 0, NOW(), 1, NOW(), 1, NULL, NULL),
    (1004, 'Spring Specials', 'Fresh deals for spring', '2025-03-01 00:00:00', '2025-03-31 23:59:59', 750, 0, NOW(), 1, NOW(), 1, NULL, NULL),
    (1005, 'New Year Bonanza', 'Welcome the new year with discounts', '2025-01-01 00:00:00', '2025-01-10 23:59:59', 100, 0, NOW(), 1, NOW(), 1, NULL, NULL);
