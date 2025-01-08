package com.bkmarriott.promotion.infrastructure.cache.operation;

import com.bkmarriott.promotion.infrastructure.cache.vo.TimeAttackVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimeAttackOperation implements RedisOperation<TimeAttackVO>{

    @Override
    public Long count(RedisOperations<String, String> operations, TimeAttackVO timeAttackVO) {
        String key = timeAttackVO.key();
        Long size = operations.opsForSet().size(key);

        log.debug("[TimeAttackOperation] [count] key ::: {}, size ::: {}", key, size);
        return size;
    }

    @Override
    public Long add(RedisOperations<String, String> operations, TimeAttackVO timeAttackVO) {
        String key = timeAttackVO.key();
        String value = this.generateValue(timeAttackVO);
        Long result = operations.opsForSet().add(key, value);

        log.info("[TimeAttackOperation] [add] key ::: {}, value ::: {}", key, value);
        return result;
    }

    @Override
    public void execute(RedisOperations<String, String> operations, TimeAttackVO timeAttackVO) {
        this.count(operations, timeAttackVO);
        this.add(operations, timeAttackVO);
    }

    @Override
    public String generateValue(TimeAttackVO timeAttackVO) {
        return String.valueOf(timeAttackVO.getUserId());
    }
}
