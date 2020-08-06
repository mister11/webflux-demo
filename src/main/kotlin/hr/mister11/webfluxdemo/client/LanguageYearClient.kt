package hr.mister11.webfluxdemo.client

import hr.mister11.webfluxdemo.service.Language
import hr.mister11.webfluxdemo.service.LanguageYear
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private val urls = mapOf(
    "Kotlin" to "https://run.mocky.io/v3/6654273e-456d-40ce-9209-c879c93a844d",
    "Java" to "https://run.mocky.io/v3/f04c90fc-7529-4f0b-a9d7-90e92105d5bf",
    "Go" to "https://run.mocky.io/v3/2eece29e-29d8-4d59-aa81-9d47886f2e17",
    "Python" to "https://run.mocky.io/v3/72bbad6e-2127-4f8a-a9c1-2fdf15b0c18c"
)

interface LanguageYearClient {
    fun fetchLanguageYear(language: Language): Mono<LanguageYear>
}

@Component
class MockyLanguageYearClient(
    private val webClient: WebClient
) : LanguageYearClient {

    override fun fetchLanguageYear(language: Language): Mono<LanguageYear> {
        return webClient
            .get()
            .uri(urls[language.name].toString())
            .retrieve()
            .bodyToMono(LanguageYear::class.java)
    }
}
