package com.gargantua7.bemfa

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import java.net.InetAddress
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val (uid, topic) = args
    println("[${LocalDateTime.now()}] uid -> $uid")
    println("[${LocalDateTime.now()}] topic -> $topic")
    runBlocking {
        // DNS解析
        val ip = withContext(Dispatchers.IO) {
            InetAddress.getByName("bemfa.com")
        }
        println("[${LocalDateTime.now()}] DNS Host -> ${ip.hostAddress}")

        // TCP连接
        val selectorManager = SelectorManager(Dispatchers.IO)
        val tcpClient = aSocket(selectorManager).tcp()
        val socket = tcpClient.connect(ip.hostAddress, 8344)
        println("[${LocalDateTime.now()}] Connect Success")

        val input = socket.openReadChannel()
        val output = socket.openWriteChannel(true)

        // 订阅
        output.writeStringUtf8("cmd=1&uid=$uid&topic=$topic\r\n")
        // 初始状态
        output.writeStringUtf8("cmd=2&uid=$uid&topic=$topic/up&msg=on\r\n")

        // 心跳定时检测
        Timer().schedule(40000, 40000) {
            CoroutineScope(Dispatchers.IO).launch {
                output.writeStringUtf8("cmd=2&uid=$uid&topic=$topic/up&msg=on\r\n")
                println("[${LocalDateTime.now()}] ping it")
            }
        }

        // 消息接收
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                input.readUTF8Line()?.let {
                    println("[${LocalDateTime.now()}] msg -> $it")
                    if ("msg=off" in it) {
                        output.writeStringUtf8("cmd=2&uid=$uid&topic=$topic/up&msg=on\r\n")
                        println("[${LocalDateTime.now()}] shutdown")
                        val cmd = buildString {
                            append("shutdown")
                            for (i in 2..args.lastIndex) {
                                append(" ${args[i]}")
                            }
                        }
                        Runtime.getRuntime().exec(cmd)
                    } else if ("msg=exit" in it) {
                        exitProcess(0)
                    }
                    Unit
                }
            }
        }
    }
}