package hr.mister11.webfluxdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class WebfluxdemoApplication

fun main(args: Array<String>) {
    runApplication<WebfluxdemoApplication>(*args)
}

@Configuration(proxyBeanMethods = false)
class WebClientConfiguration {

    @Bean
    fun webClient(): WebClient {
        return WebClient.create()
    }
}
