package com.example.reminderkotlin.util

import java.util.concurrent.atomic.AtomicInteger

object RandomIntUtil {
    private val seed = AtomicInteger()
    fun getRandom() = seed.getAndIncrement()+System.currentTimeMillis().toInt()
}