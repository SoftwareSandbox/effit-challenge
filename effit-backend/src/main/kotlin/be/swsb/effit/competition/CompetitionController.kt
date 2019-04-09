package be.swsb.effit.competition

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/competition",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class CompetitionController(private val competitionRepository: CompetitionRepository) {

    @GetMapping
    fun allCompetitions(): ResponseEntity<List<Competition>> {
        return ResponseEntity.ok(competitionRepository.findAll())
    }

    @GetMapping("{name}")
    fun competitionDetail(@PathVariable name: String): ResponseEntity<Competition> {
        return ResponseEntity.ok(competitionRepository.findByName(name))
    }
}