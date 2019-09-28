package be.swsb.effit.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebApplicationConfig: WebMvcConfigurer {
    // Paths that do not match the pattern for a static resource (containing a '.'), will be forwarded to root (and will be served the vue frontend)
    // Since it's a forward, the url will stay the same. This makes it so vue can route to the correct page.
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/**/{spring:[^.]+}").setViewName("forward:/")
    }
}