package be.swsb.effit.adapter.ui.util

import com.fasterxml.jackson.databind.ObjectMapper

fun RestApiExposed.toJson(objectMapper: ObjectMapper): String {
    return objectMapper.writeValueAsString(this)
}

fun Iterable<Any>.toJson(objectMapper: ObjectMapper): String {
    return objectMapper.writeValueAsString(this)
}
