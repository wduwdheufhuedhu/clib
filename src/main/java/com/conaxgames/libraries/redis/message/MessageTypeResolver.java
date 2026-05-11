package com.conaxgames.libraries.redis.message;

@Deprecated(forRemoval = true)
public interface MessageTypeResolver {
    MessageTypeInterface resolve(String action);
}
