package be.swsb.effit.competition

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.time.LocalDate

@RestController
@RequestMapping("/api/competition",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val competitionCreator: CompetitionCreator) {

    @GetMapping
    fun allCompetitions(): ResponseEntity<List<Competition>> {
        return ResponseEntity.ok(competitionRepository.findAll())
    }

    @GetMapping("{competitionId}")
    fun competitionDetail(@PathVariable competitionId: String): ResponseEntity<Competition> {
        return competitionRepository.findByCompetitionIdentifier(CompetitionId(competitionId))
                ?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createCompetition(@RequestBody createCompetition: CreateCompetition): ResponseEntity<Any> {
        try {
            val competitionToBeCreated = competitionCreator.from(createCompetition)
            val createdCompetition = competitionRepository.save(competitionToBeCreated)
            return ResponseEntity.created(URI(createdCompetition.id.toString())).build()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went horribly wrong", e)
        }
    }

}