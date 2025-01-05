package com.bkmarriott.hotel.infrastructure.persistence.repository;

import com.bkmarriott.hotel.domain.Hotel;
import com.bkmarriott.hotel.infrastructure.persistence.entity.HotelEntity;
import com.bkmarriott.hotel.presentation.rest.dto.request.HotelSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.bkmarriott.hotel.infrastructure.persistence.mapper.HotelEntityMapper.HOTEL_ENTITY_MAPPER;
import static com.bkmarriott.hotel.infrastructure.persistence.entity.QHotelEntity.hotelEntity;

@Repository
@RequiredArgsConstructor
public class HotelQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Hotel> searchHotel(HotelSearchRequest hotelSearchRequest, Pageable pageable){
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        List<HotelEntity> content = jpaQueryFactory
                .selectFrom(hotelEntity)
                .where(
                        hotelFilter(hotelSearchRequest)
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(hotelEntity.count())
                .from(hotelEntity)
                .where(
                        hotelEntity.isDeleted.isFalse()
                );

        List<Hotel> dtoList = HOTEL_ENTITY_MAPPER.mapToHotelList(content);

        return PageableExecutionUtils.getPage(dtoList, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder hotelFilter(HotelSearchRequest hotelSearchRequest) {
        return new BooleanBuilder()
                .and(containsHotelName(hotelSearchRequest.name()))
                .and(matchesHotelCity(hotelSearchRequest.city()))
                .and(hotelEntity.isDeleted.isFalse());
    }

    private BooleanExpression containsHotelName(String name){
        return (name == null || name.isBlank()) ? null : hotelEntity.name.contains(name);
    }

    private BooleanExpression matchesHotelCity(String city){
        return (city == null || city.isBlank()) ? null : hotelEntity.city.eq(city);
    }

    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, hotelEntity.createdAt));
                        break;
                    case "updatedAt":
                        orders.add(new OrderSpecifier<>(direction, hotelEntity.updatedAt));
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }
}
