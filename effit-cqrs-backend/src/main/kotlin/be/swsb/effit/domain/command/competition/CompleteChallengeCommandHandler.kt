package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.CommandHandler
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.stereotype.Component

@Component
class CompleteChallengeCommandHandler(
        private val queryExecutor: QueryExecutor,
        private val competitionRepository: CompetitionRepository)
    : CommandHandler<Competition, CompleteChallenge> {
    override fun handle(command: CompleteChallenge): Competition {
        val competitionId = command.id
        val challengeId = command.challengeId
        val competition = findCompetition(competitionId)
        competition.challenges.find { it.id == challengeId }
                ?.let { competition.completeChallenge(it, command.completerId.competitorId) }
                ?: throw EntityNotFoundDomainRuntimeException("Competition with id $competitionId has no challenge with id $challengeId")

        return competitionRepository.save(competition)
    }

    private fun findCompetition(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(FindCompetition(competitionId))
    }

    override fun getCommandType(): Class<CompleteChallenge> {
        return CompleteChallenge::class.java
    }
}
