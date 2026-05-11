package com.conaxgames.libraries.redis.subscription.generator;

import com.conaxgames.libraries.redis.subscription.model.JedisSubscriptionGenerator;

@Deprecated(forRemoval = true)
public class StringJedisSubscriptionGenerator implements JedisSubscriptionGenerator<String> {

    @Override
    public String generateSubscription(String message) {
        return message;
    }
}
