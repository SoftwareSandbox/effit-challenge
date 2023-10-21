package be.swsb.effit

import be.swsb.effit.plugins.*
import be.swsb.effit.ui.configureUI
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val httpClient = HttpClient(Apache) {
//    install(ContentNegotiation) {
//        json(json = Json {
//            encodeDefaults = true
//            isLenient = true
//            allowSpecialFloatingPointValues = true
//            allowStructuredMapKeys = true
//            prettyPrint = false
//            useArrayPolymorphism = false
//            ignoreUnknownKeys = true
//        })
//    }
}

fun Application.module() {
    configureHTTP()
    configureSecurity(httpClient)
//    configureSimpleSecurity(httpClient)
//    configureMonitoring()
//    configureSerialization()
//    configureDatabases()
    configureUI(httpClient)
}
