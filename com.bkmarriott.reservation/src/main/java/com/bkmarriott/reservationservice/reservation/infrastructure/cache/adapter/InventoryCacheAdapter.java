package com.bkmarriott.reservationservice.reservation.infrastructure.cache.adapter;

import com.bkmarriott.reservationservice.reservation.application.exception.NoAvailableRoomsException;
import com.bkmarriott.reservationservice.reservation.application.outputport.cache.InventoryCacheOutputPort;
import com.bkmarriott.reservationservice.reservation.domain.vo.InventoryQuery;
import com.bkmarriott.reservationservice.reservation.infrastructure.cache.util.RedisKeyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bkmarriott.reservationservice.reservation.domain.event.RoomInventoryEvent.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class InventoryCacheAdapter implements InventoryCacheOutputPort {

    private final RedisTemplate<String, Long> redisTemplate;
    private final RedissonClient redissonClient;

    @Override
    public List<RoomStockInfo> decreaseRoomCount(InventoryQuery query) {
        log.info("[InventoryCacheAdapter] [decreaseRoomCount]");
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        List<RoomStockInfo> roomStockInfoList = new ArrayList<>();

        RLock lock = redissonClient.getLock(RedisKeyFactory.LOCK_KEY);

        try{
            if(!lock.tryLock(1000, 30, TimeUnit.SECONDS)){
                throw new NoAvailableRoomsException("락 획득에 실패했습니다.");
            }

            for (LocalDate date : query.getDateRange()) {
                Long sequenceNumber = ops.increment(RedisKeyFactory.SEQUENCE_KEY);

                String key = RedisKeyFactory.generateInventoryKey(query.hotelId(), date, query.roomType());
                Long decrement = ops.decrement(key);
                roomStockInfoList.add(new RoomStockInfo(sequenceNumber, key, decrement));
                if (decrement < 0) {
                    for(RoomStockInfo past : roomStockInfoList){
                        ops.increment(past.getRedisRoomKey());
                    }
                    throw new NoAvailableRoomsException("객실 선점에 실패했습니다.");
                }
            }
        }catch (InterruptedException e) {
            throw new RuntimeException("락 처리 중 인터럽트가 발생했습니다.", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return roomStockInfoList;
    }

    @Override
    public List<RoomStockInfo> rollbackCount(InventoryQuery query) {
        log.info("[InventoryCacheAdapter] [rollbackCount]");
        ValueOperations<String, Long> ops = redisTemplate.opsForValue();
        List<RoomStockInfo> roomStockInfoList = new ArrayList<>();

        RLock lock = redissonClient.getLock(RedisKeyFactory.LOCK_KEY);

        try{
            if(!lock.tryLock(1000, 30, TimeUnit.SECONDS)){
                throw new NoAvailableRoomsException("락 획득에 실패했습니다.");
            }

            for (LocalDate date : query.getDateRange()) {
                Long sequenceNumber = ops.increment(RedisKeyFactory.SEQUENCE_KEY);
                String key = RedisKeyFactory.generateInventoryKey(query.hotelId(), date, query.roomType());
                Long increment = ops.increment(key);
                roomStockInfoList.add(new RoomStockInfo(sequenceNumber, key, increment));
            }

        }catch (InterruptedException e) {
            throw new RuntimeException("락 처리 중 인터럽트가 발생했습니다.", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return roomStockInfoList;
    }
}