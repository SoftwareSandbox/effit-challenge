package be.swsb.effit.backend.scenariotests

import be.swsb.effit.backend.domain.command.challenge.UpdateChallenge
import be.swsb.effit.backend.domain.command.competition.*
import be.swsb.effit.backend.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.backend.domain.command.competition.competitor.CompleterId
import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.query.challenge.FindChallenge
import be.swsb.effit.backend.domain.query.competition.CompetitionQueries
import be.swsb.effit.backend.messaging.command.CommandExecutor
import be.swsb.effit.backend.messaging.query.QueryExecutor
import java.util.*

class Scenarios(
    private val commandExecutor: CommandExecutor,
    private val queryExecutor: QueryExecutor,
) {

    fun createNewCompetition(competition: CreateCompetition, selectedChallenge: ChallengeToAdd? = null) : CompetitionId {
        val competitionId = commandExecutor.execute(competition).competitionId
        selectedChallenge?.let { addChallenges(competitionId, listOf(it)) }
        return competitionId
    }

    fun getCompetition(competitionId: CompetitionId): Competition {
        return queryExecutor.execute(CompetitionQueries.FindCompetition(competitionId))
    }

    fun addChallenges(competitionId: CompetitionId, challenges: List<ChallengeToAdd>) {
        commandExecutor.execute(AddChallenges(competitionId, challenges))
    }

    fun addCompetitor(competitorName: String, competitionId: CompetitionId): UUID {
        val result = commandExecutor.execute(AddCompetitor(competitionId, CompetitorName(competitorName)))
        return result.competitors.first { it.name == competitorName }.id
    }

    fun completeChallenge(competitionId: CompetitionId, challengeId: UUID, competitorId: UUID) {
//        getChallenge(challengeId) //mimick the fact that a Challenge was selected in the UI
//        val challenge = queryExecutor.execute(FindChallenge(challengeId))
        commandExecutor.execute(CompleteChallenge(competitionId, challengeId, CompleterId(competitorId)))
    }

    fun startCompetition(competitionId: CompetitionId) {
        commandExecutor.execute(StartCompetition(competitionId))
    }

    fun unstartCompetition(competitionId: CompetitionId) {
        commandExecutor.execute(UnstartCompetition(competitionId))
    }

    fun updateChallenge(updatedChallenge: Challenge) {
        commandExecutor.execute(UpdateChallenge(updatedChallenge.id, updatedChallenge.name, updatedChallenge.points, updatedChallenge.description))
    }

    fun getChallenge(requestedChallengeId: UUID): Challenge {
        return queryExecutor.execute(FindChallenge(requestedChallengeId))
    }
}
