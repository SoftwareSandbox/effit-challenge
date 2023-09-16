package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.competition.CompetitionCommands.CompleteChallenge
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

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
