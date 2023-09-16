package be.swsb.effit.plugins

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.p
import kotlinx.serialization.Serializable

fun Application.configureSecurity(httpClient: HttpClient) {

    val redirects = mutableMapOf<String, String>()

    val googleConfig = environment.config.config("oauth").config("google")
    val googleClientId = googleConfig.tryGetString("clientId") ?: "clientId"
    val googleClientSecret = googleConfig.tryGetString("clientSecret") ?: "clientSecret"
    authentication {
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
                    }
                )
            }
            client = httpClient
        }
    }
    install(Sessions) {
        cookie<UserSession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    routing {
        authenticate("auth-oauth-google") {
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
                val response: HttpResponse = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                    }
                }
                println(response.bodyAsText())
                val userInfo: UserInfo = response.body()
                call.respondHtml {
                    body {
                        div {
                            p { +"Hello, ${userInfo.name}!" }
                            img { src = userInfo.picture }
                        }
                    }
                }
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

data class UserSession(val state: String, val token: String)

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    val picture: String,
    val locale: String,
)