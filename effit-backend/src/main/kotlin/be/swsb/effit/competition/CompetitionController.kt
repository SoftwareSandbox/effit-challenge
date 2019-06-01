package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.challenge.ChallengeRepository
import be.swsb.effit.competition.competitor.CompetitorRepository
import be.swsb.effit.competition.competitor.CompleterId
import be.swsb.effit.exceptions.CompetitionAlreadyExistsDomainException
import be.swsb.effit.exceptions.EntityNotFoundDomainRuntimeException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api/competition",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class CompetitionController(private val competitionRepository: CompetitionRepository,
                            private val competitorRepository: CompetitorRepository,
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
        val createdCompetition = competitionRepository.save(competitionToBeCreated)
        return ResponseEntity.created(URI(createdCompetition.competitionId.id)).build()
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

    @PostMapping("{competitionId}/addCompetitor")
    fun addCompetitor(@PathVariable("competitionId") competitionId: String,
                      @RequestBody competitor: Competitor): ResponseEntity<Any> {
        val competition = competitionRepository.findByCompetitionIdentifier(CompetitionId(competitionId))
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id $competitionId not found")

        competition.addCompetitor(competitorRepository.save(competitor))
        competitionRepository.save(competition)

        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/complete/{challengeId}")
    fun completeChallenge(@PathVariable("competitionId") competitionId: String,
                          @PathVariable("challengeId") challengeId: UUID,
                          @RequestBody completerId: CompleterId): ResponseEntity<Any> {
        val competition = competitionRepository.findByCompetitionIdentifier(CompetitionId(competitionId))
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id $competitionId not found")

        competition.challenges.find { it.id == challengeId }
                ?.let { completeChallenge(competition, it, completerId.competitorId) }
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id $competitionId has no challenge with id $challengeId")

        competitionRepository.save(competition)

        return ResponseEntity.accepted().build()
    }

    private fun completeChallenge(competition: Competition, challenge: Challenge, competitorId: UUID) {
        competition.competitors.find { it.id == competitorId }
                ?.completeChallenge(challenge)
    }

}