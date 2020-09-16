package hr.mister11.webfluxdemo.service

import com.nhaarman.mockitokotlin2.any
import hr.mister11.webfluxdemo.client.LanguageYearClient
import hr.mister11.webfluxdemo.languages
import hr.mister11.webfluxdemo.repository.LanguageRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class LanguagesServiceTest {

    private val languageRepository: LanguageRepository = mock(LanguageRepository::class.java)
    private val languageYearClient: LanguageYearClient = mock(LanguageYearClient::class.java)

    lateinit var languagesService: LanguagesService

    @BeforeEach
    fun setUp() {
        languagesService = LanguagesService(languageYearClient, languageRepository)
    }

    @Test
    @DisplayName("Get languages should return combination of stored names and fetched years")
    fun getLanguagesTest() {
        `when`(languageRepository.fetchLanguages()).thenReturn(Flux.fromIterable(languages))
        `when`(languageYearClient.fetchLanguageYear(any())).thenReturn(Mono.just(LanguageYear(2000)))

        val expectedLanguages = listOf(
            Language(name = "Test language 1", year = 2000),
            Language(name = "Test language 2", year = 2000),
            Language(name = "Test language 3", year = 2000)
        )
        StepVerifier.create(languagesService.getLanguages())
            .expectNextSequence(expectedLanguages)
            .verifyComplete()
    }
}
