create table m_default_room_charge (
                                       id bigint not null auto_increment,
                                       created_at datetime(6),
                                       created_by bigint,
                                       deleted_at datetime(6),
                                       deleted_by bigint,
                                       is_deleted bit default false,
                                       updated_at datetime(6),
                                       updated_by bigint,
                                       charge integer not null,
                                       room_type enum ('DELUXE','STANDARD','SUITE','TWIN') not null,
                                       primary key (id)
);
create table m_hotel_type (
                              id bigint not null auto_increment,
                              created_at datetime(6),
                              created_by bigint,
                              deleted_at datetime(6),
                              deleted_by bigint,
                              is_deleted bit default false,
                              updated_at datetime(6),
                              updated_by bigint,
                              hotel_id bigint not null,
                              room_type enum ('DELUXE','STANDARD','SUITE','TWIN'),
                              primary key (id)
);
create table m_room_charge (
                               date date not null,
                               hotel_id bigint not null,
                               room_type enum ('DELUXE','STANDARD','SUITE','TWIN') not null,
                               created_at datetime(6),
                               created_by bigint,
                               deleted_at datetime(6),
                               deleted_by bigint,
                               is_deleted bit default false,
                               updated_at datetime(6),
                               updated_by bigint,
                               charge integer not null,
                               primary key (date, hotel_id, room_type)
);