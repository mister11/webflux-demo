package hr.mister11.webfluxdemo.service

import hr.mister11.webfluxdemo.client.LanguageYearClient
import hr.mister11.webfluxdemo.repository.LanguageRepository
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class LanguagesService(
    private val languageYearClient: LanguageYearClient,
    private val languageRepository: LanguageRepository
) {

    fun getLanguages(): Flux<Language> {
        return languageRepository.fetchLanguages()
            .flatMap { language ->
                languageYearClient.fetchLanguageYear(language)
                    .map { languageYear -> language.copy(year = languageYear.year) }
            }
    }

}

@Table("languages")
data class Language(
    @Id val id: Long = 0,
    @Column("name") val name: String,
    @Column("year") val year: Int? = null
)

data class LanguageYear(
    val year: Int
)
