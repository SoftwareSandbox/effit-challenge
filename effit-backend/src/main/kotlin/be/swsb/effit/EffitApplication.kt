package be.swsb.effit

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
class EffitApplication {

    fun main(args: Array<String>) {
        SpringApplication.run(EffitApplication::class.java, *args)
    }
}
