package be.swsb.effit

import be.swsb.effit.plugins.UserInfo
import be.swsb.effit.plugins.UserSession
import be.swsb.effit.plugins.configureSecurity
import be.swsb.effit.ui.configureUI
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import org.approvaltests.Approvals
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testSecurity() = testApplication {
        environment {
            config = ApplicationConfig("application-custom.yaml")
        }
        val testHttpClient = createClient {
            install(HttpCookies)
            install(ContentNegotiation) {
                json()
            }
        }
        application {
            configureSecurity(testHttpClient)
            configureUI(testHttpClient)
        }

        routing {
            get("/login-test") {
                call.sessions.set(UserSession("xyzABC123","abc123"))
            }
        }

        externalServices {
            hosts("https://www.googleapis.com") {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                    json()
                }
                routing {
                    get("oauth2/v2/userinfo") {
                        call.respond(UserInfo("1", "JetBrains", "image-url", ""))
                    }
                }
            }
        }
        val loginResponse = testHttpClient.get("/login-test")
        val helloResponse = testHttpClient.get("/")

        Approvals.verify(helloResponse.bodyAsText())
    }
}
