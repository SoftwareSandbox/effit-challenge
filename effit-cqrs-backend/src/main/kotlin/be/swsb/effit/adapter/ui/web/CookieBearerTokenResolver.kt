package org.scrambled.common.adapter.restapi.security

import be.swsb.effit.adapter.ui.util.removeBearer
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


private const val JwtCookieName = "CRUMBLE"

class CookieBearerTokenResolver : BearerTokenResolver {
    override fun resolve(request: HttpServletRequest?): String? =
        request?.cookies?.firstOrNull { it.name == JwtCookieName }?.value
            ?: DefaultBearerTokenResolver().resolve(request)
    //TODO: if the JWT on the cookie has expired, fallback to the JWT on the Authorization header
}

class CookieBearerTokenResolverForTest : BearerTokenResolver {
    override fun resolve(request: HttpServletRequest?): String? =
        request?.cookies?.firstOrNull { it.name == JwtCookieName }?.value
            ?: request?.getHeader(HttpHeaders.AUTHORIZATION)?.removeBearer()
}

@RestController
@RequestMapping("/api/session")
class SessionController() {
    @GetMapping
    fun exchangeAuthHeaderForCookie(): ResponseEntity<Any> {
        val crumble = ResponseCookie.from(JwtCookieName, getJwtAsStringFromSecurityContext())
            .httpOnly(true)
            .secure(false) //TODO: probably set this to true if we're hosting on https
            .path("/")
            .maxAge(6000) //TODO: align with JWT ttl?
            .domain("localhost")
            .build()
        return ResponseEntity.accepted()
            .header(HttpHeaders.SET_COOKIE, crumble.toString())
            .build()
    }

    private fun getJwtAsStringFromSecurityContext() =
        (SecurityContextHolder.getContext().authentication as JwtAuthenticationToken).token.tokenValue
}
