package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.command.CommandHandler
import be.swsb.effit.backend.domain.command.competition.CompetitionCommands.RemoveChallenge
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries.FindCompetition
import be.swsb.effit.backend.messaging.query.QueryExecutor

class RemoveChallengeCommandHandler(
    private val queryExecutor: QueryExecutor,
    private val competitionRepository: CompetitionRepository)
    : CommandHandler<Competition, RemoveChallenge> {
    override fun handle(command: RemoveChallenge): Competition {
        val competition = queryExecutor.execute(FindCompetition(command.id))
        competition.removeChallenge(command.challengeId)
        return competitionRepository.save(competition)
    }

    override fun getCommandType(): Class<RemoveChallenge> {
        return RemoveChallenge::class.java
    }
}
