package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.command.Command
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.competitor.CompetitorName
import java.time.LocalDate

sealed class CompetitionCommands {
    data class CreateCompetition(val name: String?,
                                 val startDate: LocalDate?,
                                 val endDate: LocalDate?): RestApiExposed, Command<Competition>
    data class StartCompetition(val id: CompetitionId): Command<Competition>
    data class UnstartCompetition(val id: CompetitionId): Command<Competition>
    data class AddChallenges(val id: CompetitionId, val challengesToAdd: List<ChallengeToAdd>): Command<Competition>
    data class AddCompetitor(val id: CompetitionId, val competitorToAdd: CompetitorName): Command<Competition>
}

typealias CreateCompetition = CompetitionCommands.CreateCompetition
typealias StartCompetition = CompetitionCommands.StartCompetition
typealias UnstartCompetition = CompetitionCommands.UnstartCompetition
typealias AddChallenges = CompetitionCommands.AddChallenges
typealias AddCompetitor = CompetitionCommands.AddCompetitor

