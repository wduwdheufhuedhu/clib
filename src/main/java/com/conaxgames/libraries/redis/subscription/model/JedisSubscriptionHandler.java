package com.conaxgames.libraries.redis.subscription.model;

import com.conaxgames.libraries.redis.pubsub.SubscribeObject;

@Deprecated(forRemoval = true)
public interface JedisSubscriptionHandler<K> {

    void subscribe(K object, SubscribeObject subscription);

}
