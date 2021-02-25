package hr.mister11.webfluxdemo.integration

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.language.bm.Lang
import hr.mister11.webfluxdemo.client.LanguageYearClient
import hr.mister11.webfluxdemo.repository.LanguageRepository
import hr.mister11.webfluxdemo.service.Language
import hr.mister11.webfluxdemo.service.LanguageYear
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.delete
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.core.insert
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Mono

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LanguagesIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var languageYearClient: LanguageYearClient

    @Autowired
    lateinit var r2dbcEntityTemplate: R2dbcEntityTemplate

    @BeforeEach
    fun setUp() {
        // create table on the first run
        r2dbcEntityTemplate.databaseClient.sql("""
            CREATE TABLE IF NOT EXISTS languages (
                id bigserial PRIMARY KEY,
                name text NOT NULL,
                year integer
            );
        """.trimIndent()
        ).then().block()
        // delete all data from previous run
        r2dbcEntityTemplate.delete<Language>().all().block()
        // insert new mock data
        r2dbcEntityTemplate
            .insert(Language(name = "test 1"))
            .block()
        r2dbcEntityTemplate
            .insert(Language(name = "test 2"))
            .block()
    }

    @Test
    @DisplayName("Getting all languages should return 200 with the list of all languages with correct years")
    fun getAllLanguagesTest() {
        `when`(languageYearClient.fetchLanguageYear(Language(id = 1, name = "test 1")))
            .thenReturn(Mono.just(LanguageYear(2010)))
        `when`(languageYearClient.fetchLanguageYear(Language(id = 2, name = "test 2")))
            .thenReturn(Mono.just(LanguageYear(2020)))
        webTestClient
            .get()
            .uri("/languages")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<Language>()
            .contains(*listOf(
                Language(id = 1, name = "test 1", year = 2010),
                Language(id = 2, name = "test 2", year = 2020)
            ).toTypedArray())
    }
}
