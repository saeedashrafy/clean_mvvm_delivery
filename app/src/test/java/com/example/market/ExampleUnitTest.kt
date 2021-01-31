package com.example.market

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    private val channel = BroadcastChannel<Boolean>(1)

    @Test
    fun broadcastChannelTest() {
        runBlocking {
            // 1. Send event
            channel.send(true)

            // 2. Start collecting
            channel
                .asFlow()
                .collect {
                    println(it)
                }

            // 3. Send another event
            channel.send(false)
        }
    }
}
