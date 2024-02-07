package com.example

import com.example.plugins.*
import com.example.repository.DatabaseFactory
import com.sun.tools.javac.file.Locations
import io.ktor.server.application.*
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
//    install (Locations)
}

fun Application.module() {

    DatabaseFactory.init()
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
