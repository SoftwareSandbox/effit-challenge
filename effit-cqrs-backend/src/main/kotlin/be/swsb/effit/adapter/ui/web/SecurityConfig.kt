package be.swsb.effit.adapter.ui.web

import be.swsb.effit.adapter.ui.util.asJwt
import org.scrambled.common.adapter.restapi.security.CookieBearerTokenResolver
import org.scrambled.common.adapter.restapi.security.CookieBearerTokenResolverForTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.oauth2.jwt.JwtDecoder


@EnableWebSecurity
@Configuration
@Profile("!test")
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http {
            authorizeRequests {
                authorize(HttpMethod.GET,"/api/competition", anonymous)
                authorize(HttpMethod.GET,"/api/competition/**", anonymous)
                authorize(HttpMethod.GET,"/api/challenge/**", anonymous)
                authorize("/api/**", authenticated)
                authorize(anyRequest, anonymous)
            }
            oauth2ResourceServer {
                jwt {
                    bearerTokenResolver = CookieBearerTokenResolver()
                }
            }
//            csrf { }
        }
    }
}

@EnableWebSecurity
@Configuration
@Profile("test")
class SecurityConfigForScenario : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http {
            authorizeRequests {
                authorize("/api/**", authenticated)
                authorize(anyRequest, anonymous)
            }
            oauth2ResourceServer {
                jwt {
                    bearerTokenResolver = CookieBearerTokenResolverForTest()
                    jwtDecoder = JwtDecoder { authHeader -> authHeader.asJwt() }
                }
            }
        }
    }
}
//JwtAuthenticationProvider
//We might need to set up/configure a `AuthenticationEntryPoint` to return a 401 (unauthorized) response when the UI sends ILLEGAL JWT on the cookie.
//We also might need to set up/configure a `AuthenticationEntryPoint` to not go off on a valid JWT
//I think this because I read "Spring Security typically requests the credentials using AuthenticationEntryPoint"
//seems interesting: https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#oauth2login-advanced-idtoken-verify

//We might need a specific BearerTokenAuthenticationFilter that extracts the Bearer token out of a cookie

// iss: http://localhost:7070/auth/realms/scrambled
//      http://localhost:7070/auth/realms/scrambled/.well-known/openid-configuration
// this might have impact on whether or not we can use issuer-uri as a resourceserver springboot config

//what we need to do:
// ✅ configure our rest-api to be a resourceserver
//  configure the properties for that (maybe we need manual config if issuer-uri doesn't magically work, for that see https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#oauth2resourceserver-jwt-jwkseturi )
//  customize a `BearerTokenResolver` to extract the JWT out of a cookie, instead of expecting it in the `Authorization: Bearer` header
//  see https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#oauth2resourceserver-bearertoken-resolver

//if we ever get to adding roles, we might use a `CustomAuthenticationConverter` which gets a Known based on the JWT.iss+sub
//and understands how to map roles we keep internally to GrantedAuthorities
//see https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#oauth2resourceserver-jwt-authorization-extraction