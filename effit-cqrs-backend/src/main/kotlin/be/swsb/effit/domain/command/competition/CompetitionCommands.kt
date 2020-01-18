package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.command.Command
import be.swsb.effit.domain.command.competition.competitor.CompetitorId
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import java.time.LocalDate
import java.util.*

sealed class CompetitionCommands {
    data class CreateCompetition(val name: String?,
                                 val startDate: LocalDate?,
                                 val endDate: LocalDate?): RestApiExposed, Command<Competition>
    data class StartCompetition(val id: CompetitionId): Command<Competition>
    data class UnstartCompetition(val id: CompetitionId): Command<Competition>
    data class AddChallenges(val id: CompetitionId, val challengesToAdd: List<ChallengeToAdd>): Command<Competition>
    data class RemoveChallenge(val id: CompetitionId, val challengeId: UUID): Command<Competition>
    data class AddCompetitor(val id: CompetitionId, val competitorToAdd: CompetitorName): Command<Competition>
    data class RemoveCompetitor(val id: CompetitionId, val competitorIdToRemove: CompetitorId): Command<Competition>
    data class CompleteChallenge(val id: CompetitionId, val challengeId: UUID, val completerId: CompleterId): Command<Competition>
}

typealias CreateCompetition = CompetitionCommands.CreateCompetition
typealias StartCompetition = CompetitionCommands.StartCompetition
typealias UnstartCompetition = CompetitionCommands.UnstartCompetition
typealias AddChallenges = CompetitionCommands.AddChallenges
typealias RemoveChallenge = CompetitionCommands.RemoveChallenge
typealias AddCompetitor = CompetitionCommands.AddCompetitor
typealias RemoveCompetitor = CompetitionCommands.RemoveCompetitor
typealias CompleteChallenge = CompetitionCommands.CompleteChallenge

