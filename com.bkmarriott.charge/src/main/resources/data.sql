INSERT INTO m_default_room_charge (room_type, charge)
VALUES ('STANDARD', 100000),
       ('TWIN', 120000),
       ('DELUXE', 200000),
       ('SUITE', 300000);

INSERT INTO m_hotel_type (hotel_id, room_type)
VALUES (1L, 'STANDARD'),
       (1L, 'DELUXE'),
       (1L, 'TWIN'),
       (1L, 'SUITE');
