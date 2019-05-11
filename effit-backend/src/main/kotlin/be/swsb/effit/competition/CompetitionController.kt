package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.challenge.ChallengeRepository
import be.swsb.effit.exceptions.CompetitionAlreadyExistsDomainException
import be.swsb.effit.exceptions.EntityNotFoundDomainRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI

@RestController
@RequestMapping("/api/competition",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val challengeRepository: ChallengeRepository,
                            private val competitionCreator: CompetitionCreator) {

    @GetMapping
    fun allCompetitions(): ResponseEntity<List<Competition>> {
        return ResponseEntity.ok(competitionRepository.findAll())
    }

    @GetMapping("{competitionId}")
    fun competitionDetail(@PathVariable competitionId: String): ResponseEntity<Competition> {
        return competitionRepository.findByCompetitionIdentifier(CompetitionId(competitionId))
                ?.let { ResponseEntity.ok(it) }
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id $competitionId not found")
    }

    @PostMapping
    fun createCompetition(@RequestBody createCompetition: CreateCompetition): ResponseEntity<Any> {
        val competitionToBeCreated = competitionCreator.from(createCompetition)
        competitionRepository.findByCompetitionIdentifier(competitionToBeCreated.competitionId)
                ?.let { throw CompetitionAlreadyExistsDomainException(competitionToBeCreated.competitionId) }
        try {
            val createdCompetition = competitionRepository.save(competitionToBeCreated)
            return ResponseEntity.created(URI(createdCompetition.competitionId.id)).build()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went horribly wrong", e)
        }
    }

    @PostMapping("{competitionId}/addChallenges")
    fun addChallenges(@PathVariable competitionId: String,
                      @RequestBody challengesToBeAdded: List<Challenge>): ResponseEntity<Any> {
        return competitionRepository.findByCompetitionIdentifier(CompetitionId(competitionId))
                ?.let { addChallengesAndSaveCompetition(it, challengesToBeAdded) }
                ?: ResponseEntity.notFound().build()
    }

    private fun addChallengesAndSaveCompetition(foundCompetition: Competition, challengesToBeAdded: List<Challenge>): ResponseEntity<Any> {
        challengesToBeAdded.forEach {
            val persistedChallenge = challengeRepository.save(it)
            foundCompetition.addChallenge(persistedChallenge)
        }
        competitionRepository.save(foundCompetition)
        return ResponseEntity.accepted().build()
    }

}