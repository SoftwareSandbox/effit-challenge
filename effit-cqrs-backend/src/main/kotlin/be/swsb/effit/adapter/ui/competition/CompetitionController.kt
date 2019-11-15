package be.swsb.effit.adapter.ui.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.domain.command.competition.*
import be.swsb.effit.domain.command.competition.competitor.CompetitorId
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.domain.query.competition.FindAllCompetitions
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.command.CommandExecutor
import be.swsb.effit.messaging.query.QueryExecutor
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
                            private val commandExecutor: CommandExecutor,
                            private val queryExecutor: QueryExecutor) {

    @GetMapping
    fun allCompetitions(): ResponseEntity<List<Competition>> {
        return ResponseEntity.ok(queryExecutor.execute(FindAllCompetitions))
    }

    @GetMapping("{competitionId}")
    fun competitionDetail(@PathVariable competitionId: String): ResponseEntity<Competition> {
        return ResponseEntity.ok(findCompetition(competitionId))
    }

    @PostMapping
    fun createCompetition(@RequestBody createCompetition: CreateCompetition): ResponseEntity<Any> {
        val createdCompetition = commandExecutor.execute(createCompetition)
        return ResponseEntity.created(URI(createdCompetition.competitionId.id)).build()
    }

    @PostMapping("{competitionId}/start")
    fun startCompetition(@PathVariable competitionId: String): ResponseEntity<Any> {
        commandExecutor.execute(StartCompetition(CompetitionId(competitionId)))
        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/unstart")
    fun unstartCompetition(@PathVariable competitionId: String): ResponseEntity<Any> {
        commandExecutor.execute(UnstartCompetition(CompetitionId(competitionId)))
        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/addChallenges")
    fun addChallenges(@PathVariable competitionId: String,
                      @RequestBody challengesToBeAdded: List<ChallengeToAdd>): ResponseEntity<Any> {
        commandExecutor.execute(AddChallenges(CompetitionId(competitionId), challengesToBeAdded))
        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/addCompetitor")
    fun addCompetitor(@PathVariable("competitionId") competitionId: String,
                      @RequestBody competitorName: CompetitorName): ResponseEntity<Any> {
        commandExecutor.execute(AddCompetitor(CompetitionId(competitionId), competitorName))
        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/removeCompetitor")
    fun removeCompetitor(@PathVariable("competitionId") competitionId: String,
                         @RequestBody competitorToBeRemoved: CompetitorId): ResponseEntity<Any> {
        val competition = findCompetition(competitionId)

        competition.removeCompetitor(competitorToBeRemoved.id)
        competitionRepository.save(competition)

        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/complete/{challengeId}")
    fun completeChallenge(@PathVariable("competitionId") competitionId: String,
                          @PathVariable("challengeId") challengeId: UUID,
                          @RequestBody completerId: CompleterId): ResponseEntity<Any> {
        val competition = findCompetition(competitionId)
        competition.challenges.find { it.id == challengeId }
                ?.let { competition.completeChallenge(it, completerId.competitorId) }
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id $competitionId has no challenge with id $challengeId")

        competitionRepository.save(competition)

        return ResponseEntity.accepted().build()
    }

    @PostMapping("{competitionId}/removeChallenge/{challengeId}")
    fun removeChallenge(@PathVariable("competitionId") competitionId: String,
                        @PathVariable("challengeId") challengeId: UUID): ResponseEntity<Any> {
        val competition = findCompetition(competitionId)
        competition.removeChallenge(challengeId)
        competitionRepository.save(competition)

        return ResponseEntity.ok().build<Any>()
    }

    private fun findCompetition(competitionId: String): Competition {
        return queryExecutor.execute(FindCompetition(CompetitionId(competitionId)))
    }

}
