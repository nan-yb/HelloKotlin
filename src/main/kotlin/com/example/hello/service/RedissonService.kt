package com.example.hello.service

import jakarta.annotation.PostConstruct
import org.redisson.Redisson
import org.redisson.api.RAtomicLongReactive
import org.redisson.api.RLockReactive
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RedissonService {

    @Value("127.0.0.1")
    private lateinit var redisHost: String;
    private val config = Config()
    private lateinit var redissonClient: RedissonReactiveClient
    private lateinit var redisUri: String

    @PostConstruct
    fun initialize() {
        redisUri = "redis://$redisHost:6379"
        config.useSingleServer().address = redisUri
        redissonClient = Redisson.createReactive(config)
    }

    fun getLock(lockName : String) : RLockReactive = redissonClient.getLock(lockName)

    fun getAtomicLong(key : String) : RAtomicLongReactive = redissonClient.getAtomicLong(key);

}