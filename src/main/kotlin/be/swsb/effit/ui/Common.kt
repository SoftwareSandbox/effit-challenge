package be.swsb.effit.ui

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.util.*

interface Command {
    val url: String
}

interface Query {
    val url: String
}

inline fun <reified R : Any> Parameters.getOrNull(name: String): R? = try {
    getOrFail<R>(name)
} catch (e: MissingRequestParameterException) {
    null
}