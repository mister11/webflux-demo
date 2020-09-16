package hr.mister11.webfluxdemo.controller

import hr.mister11.webfluxdemo.languages
import hr.mister11.webfluxdemo.service.Language
import hr.mister11.webfluxdemo.service.LanguagesService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.core.publisher.Flux

@WebFluxTest
class LanguagesControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var languagesService: LanguagesService

    @Test
    @DisplayName("Getting all languages should return 200 with the list of all languages from the service")
    fun getAllLanguagesTest() {
        `when`(languagesService.getLanguages()).thenReturn(Flux.fromIterable(languages))

        webTestClient
            .get()
            .uri("/languages")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Language>()
            .contains(*languages.toTypedArray())
    }

    @Test
    @DisplayName("Getting all languages should throw an error if service throws an error")
    fun getAllLanguagesErrorTest() {
        `when`(languagesService.getLanguages()).thenReturn(Flux.error(Throwable("My custom error")))

        webTestClient
            .get()
            .uri("/languages")
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody()
            .jsonPath(".status")
            .isEqualTo(500)
            .jsonPath(".error")
            .isEqualTo("Internal Server Error")
    }
}
