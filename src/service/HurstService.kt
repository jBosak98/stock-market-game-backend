package com.ktor.stock.market.game.jbosak.service

import cn.zenliu.ktor.redis.RedisFactory
import com.google.gson.Gson
import com.ktor.stock.market.game.jbosak.model.Hurst
import com.ktor.stock.market.game.jbosak.model.HurstWrapper
import io.lettuce.core.codec.StringCodec

fun getHurst(ticker:String): Hurst? {
    val redisClient = RedisFactory.newClient(StringCodec.UTF8)
    val plainHurst = redisClient.get(ticker)

    try {
        val hurstWrapped = Gson().fromJson(plainHurst, HurstWrapper::class.java)
        return hurstWrapped.hurst
    }catch (e:Exception){
    }

    return null
}