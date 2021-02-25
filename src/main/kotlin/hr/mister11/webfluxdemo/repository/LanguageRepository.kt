package hr.mister11.webfluxdemo.repository

import hr.mister11.webfluxdemo.service.Language
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.select
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

interface LanguageRepository {
    fun fetchLanguages(): Flux<Language>
}

@Repository
class ReactiveLanguageRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
//    private val databaseClient: DatabaseClient
) : LanguageRepository {

    override fun fetchLanguages(): Flux<Language> {
        return r2dbcEntityTemplate
            .select<Language>()
            .from("languages")
            .all()

//        return databaseClient.select().from("languages").`as`(Language::class.java).fetch().all()
    }
}
