package com.bkmarriott.promotion.infrastructure.cache.operation;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

@Component
public class RedisTransaction {

    public <T> List execute(
        RedisOperations<String, String> redisTemplate, RedisOperation<T> operation, T vo
    ) {
        return redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List execute(RedisOperations callbackOperation) throws DataAccessException {
                callbackOperation.multi();
                operation.execute(callbackOperation, vo);
                return callbackOperation.exec();
            }
        });
    }
}
