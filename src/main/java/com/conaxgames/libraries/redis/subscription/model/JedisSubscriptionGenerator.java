package com.conaxgames.libraries.redis.subscription.model;

@Deprecated(forRemoval = true)
public interface JedisSubscriptionGenerator<K> {

    K generateSubscription(String message);

}
