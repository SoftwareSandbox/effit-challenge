package be.swsb.effit

import be.swsb.effit.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import kotlinx.html.HTML
import org.w3c.dom.Document
import kotlin.test.*

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

        val expectedHtmlString = """
            <!DOCTYPE html>
            <html>
              <body>
                <div>
                  <p>Hello, JetBrains!</p>
            <img src="image-url"></div>
              </body>
            </html>
            
        """.trimIndent()

        assertEquals(expectedHtmlString, helloResponse.bodyAsText())
    }
}
