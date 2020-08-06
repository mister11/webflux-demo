package hr.mister11.webfluxdemo.controller

import hr.mister11.webfluxdemo.service.Language
import hr.mister11.webfluxdemo.service.LanguagesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RequestMapping("/languages")
@RestController
class LanguagesController(
    private val languagesService: LanguagesService
) {

    @GetMapping
    fun getLanguages(): Flux<Language> {
        return languagesService.getLanguages()
    }
}
