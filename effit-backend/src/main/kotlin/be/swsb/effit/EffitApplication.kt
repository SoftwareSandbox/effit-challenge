package be.swsb.effit

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
class EffitApplication

fun main(args: Array<String>) {
    SpringApplication.run(EffitApplication::class.java, *args)
}
