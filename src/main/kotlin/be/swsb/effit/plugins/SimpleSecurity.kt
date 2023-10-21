package be.swsb.effit.plugins

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import kotlinx.serialization.Serializable

fun Application.configureSimpleSecurity(httpClient: HttpClient) {

    val redirects = mutableMapOf<String, String>()

    val googleConfig = environment.config.config("oauth").config("google")
    val keycloakConfig = environment.config.config("oauth").config("keycloak")
    val googleClientId = googleConfig.tryGetString("clientId") ?: "clientId"
    val googleClientSecret = googleConfig.tryGetString("clientSecret") ?: "clientSecret"
    val keycloakClientId = keycloakConfig.tryGetString("clientId") ?: "clientId"
    val keycloakClientSecret = keycloakConfig.tryGetString("clientSecret") ?: "clientSecret"

    authentication {
//        google(googleClientId, googleClientSecret, redirects, httpClient)
        keycloak(keycloakClientId, keycloakClientSecret, redirects, httpClient)
    }

    install(Sessions) {
        cookie<UserSession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    routing {
        authenticate("auth-oauth-keycloak") {
            get("login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
                val redirect = redirects[principal.state!!]
                call.respondRedirect(redirect!!)
            }
        }
        get("/{path}") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                    }
                }.body()
                call.respondText("Hello, ${userInfo.name}!")
            } else {
                val redirectUrl = URLBuilder("http://0.0.0.0:8080/login").run {
                    parameters.append("redirectUrl", call.request.uri)
                    build()
                }
                call.respondRedirect(redirectUrl)
            }
        }
    }
}

private fun AuthenticationConfig.keycloak(
    clientId: String,
    clientSecret: String,
    redirects: MutableMap<String, String>,
    httpClient: HttpClient
) {
    oauth("auth-oauth-keycloak") {
        urlProvider = { "http://localhost:8080/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "keycloak",
                authorizeUrl = "http://localhost:9090/realms/effit-ktor/protocol/openid-connect/auth",
                accessTokenUrl = "http://localhost:9090/realms/effit-ktor/protocol/openid-connect/token",
                requestMethod = HttpMethod.Post,
                clientId = clientId,
                clientSecret = clientSecret,
                defaultScopes = listOf("profile"),
                extraAuthParameters = listOf("access_type" to "offline"),
                onStateCreated = { call, state ->
                    redirects[state] =
                        call.request.queryParameters["redirectUrl"] ?: error("No redirectUrl query param")
                },
            )
        }
        client = httpClient
    }
}
private fun AuthenticationConfig.google(
    googleClientId: String,
    googleClientSecret: String,
    redirects: MutableMap<String, String>,
    httpClient: HttpClient
) {
    oauth("auth-oauth-google") {
        urlProvider = { "http://localhost:8080/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "google",
                authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                requestMethod = HttpMethod.Post,
                clientId = googleClientId,
                clientSecret = googleClientSecret,
                defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                extraAuthParameters = listOf("access_type" to "offline"),
                onStateCreated = { call, state ->
                    redirects[state] =
                        call.request.queryParameters["redirectUrl"] ?: error("No redirectUrl query param")
                },
            )
        }
        client = httpClient
    }
}

data class UserSession2(val state: String, val token: String)

@Serializable
data class UserInfo2(
    val id: String,
    val name: String,
    val picture: String,
    val locale: String,
)