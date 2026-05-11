package com.conaxgames.libraries.redis.storage;

@Deprecated(forRemoval = true)
public interface RedisCommand<T> {

    void execute(T t);
}
