package hr.mister11.webfluxdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.sql.ResultSet

@SpringBootApplication
class WebfluxdemoApplication

fun main(args: Array<String>) {
    runApplication<WebfluxdemoApplication>(*args)
}

val urls = mapOf(
    "Kotlin" to "https://run.mocky.io/v3/6654273e-456d-40ce-9209-c879c93a844d",
    "Java" to "https://run.mocky.io/v3/f04c90fc-7529-4f0b-a9d7-90e92105d5bf",
    "Go" to "https://run.mocky.io/v3/2eece29e-29d8-4d59-aa81-9d47886f2e17",
    "Python" to "https://run.mocky.io/v3/72bbad6e-2127-4f8a-a9c1-2fdf15b0c18c"
)

@RequestMapping("/languages")
@RestController
class LanguagesController(
    private val jdbcTemplate: JdbcTemplate,
    private val webClient: WebClient
) {

    @GetMapping
    fun getLanguages(): List<Language> {
        val languages = jdbcTemplate.query("select * from languages") { rs: ResultSet, _: Int ->
            Language(name = rs.getString("name"))
        };

        return languages.map { language ->
            val languageYear = webClient
                .get()
                .uri(urls[language.name].toString())
                .retrieve()
                .bodyToMono(LanguageYear::class.java)
                .block()
            language.copy(year = languageYear?.year)
        }
    }
}

data class Language(
    val name: String,
    val year: Int? = null
)

data class LanguageYear(
    val year: Int
)


@Configuration(proxyBeanMethods = false)
class WebClientConfiguration {

    @Bean
    fun webClient(): WebClient {
        return WebClient.create()
    }
}
