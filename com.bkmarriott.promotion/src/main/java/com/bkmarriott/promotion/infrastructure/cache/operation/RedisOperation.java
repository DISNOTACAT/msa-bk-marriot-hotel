package com.bkmarriott.promotion.infrastructure.cache.operation;

import org.springframework.data.redis.core.RedisOperations;

public interface RedisOperation<T> {

    Long count(RedisOperations<String, String> operations, T t);

    Long add(RedisOperations<String, String> operations, T t);

    void execute(RedisOperations<String, String> operations, T t);

    String generateValue(T t);
}
