package be.swsb.effit.adapter.ui.util

import be.swsb.effit.security.ExternalAccountRef
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Instant
import java.util.*

fun SecurityContext.toExternalAccountRef(): ExternalAccountRef {
    val jwt = (this.authentication as JwtAuthenticationToken).token
    return ExternalAccountRef(jwt.issuer.toString(), jwt.subject)
}

internal fun String.removeBearer() = this.removePrefix("Bearer ")

internal fun String.asJwt(): org.springframework.security.oauth2.jwt.Jwt {
    return this.asJwtJson().asJwt(this)
}

private fun String.asJwtJson(): JwtForTest {
    val encodedBearerToken = this.substringAfter("Bearer ").split(".")
    val jwtPayload = encodedBearerToken[1]
    val decodedJwt = Base64.getUrlDecoder().decode(jwtPayload).decodeToString()
    return jacksonObjectMapper().readValue(decodedJwt)
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class JwtForTest(
    private val iss: String,
    private val sub: String,
) {

    fun asJwt(originalString: String): org.springframework.security.oauth2.jwt.Jwt {
        return org.springframework.security.oauth2.jwt.Jwt(
            originalString,
            Instant.now(),
            Instant.now(),
            mutableMapOf("snarf" to "snarf") as Map<String, String>,
            mutableMapOf("iss" to iss, "sub" to sub) as Map<String, Any>
        )
    }
}
