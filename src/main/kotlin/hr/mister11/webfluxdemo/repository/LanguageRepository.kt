package hr.mister11.webfluxdemo.repository

import hr.mister11.webfluxdemo.service.Language
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

interface LanguageRepository {
    fun fetchLanguages(): Flux<Language>
}

@Repository
class ReactiveLanguageRepository(
    private val databaseClient: DatabaseClient
) : LanguageRepository {

    override fun fetchLanguages(): Flux<Language> {
        return databaseClient.select().from("languages").`as`(Language::class.java).fetch().all()
    }
}
