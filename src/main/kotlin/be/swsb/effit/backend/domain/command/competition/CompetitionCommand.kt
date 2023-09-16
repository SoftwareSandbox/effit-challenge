package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.RestApiExposed
import be.swsb.effit.backend.messaging.command.Command
import be.swsb.effit.backend.domain.command.competition.competitor.CompetitorId
import be.swsb.effit.backend.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.backend.domain.command.competition.competitor.CompleterId
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import java.time.LocalDate
import java.util.*

sealed interface CompetitionCommand : Command<Competition>

data class CreateCompetition(
    val name: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?
) : CompetitionCommand, RestApiExposed

data class StartCompetition(val id: CompetitionId) : CompetitionCommand
data class UnstartCompetition(val id: CompetitionId) : CompetitionCommand

data class AddChallenges(
    val id: CompetitionId,
    val challengesToAdd: List<ChallengeToAdd>
) : CompetitionCommand

data class RemoveChallenge(
    val id: CompetitionId,
    val challengeId: UUID
) : CompetitionCommand

data class AddCompetitor(
    val id: CompetitionId,
    val competitorToAdd: CompetitorName
) : CompetitionCommand

data class RemoveCompetitor(
    val id: CompetitionId,
    val competitorIdToRemove: CompetitorId
) : CompetitionCommand

data class CompleteChallenge(
    val id: CompetitionId,
    val challengeId: UUID,
    val completerId: CompleterId
) : CompetitionCommand