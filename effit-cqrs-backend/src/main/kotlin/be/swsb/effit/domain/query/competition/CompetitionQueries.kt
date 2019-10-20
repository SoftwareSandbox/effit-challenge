package be.swsb.effit.domain.query.competition

import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.query.Query

sealed class CompetitionQueries {
    object FindAllCompetitions: Query<List<Competition>>
    data class FindCompetition(val id: CompetitionId): Query<Competition>
    data class MaybeFindCompetition(val id: CompetitionId): Query<Competition?>
}

typealias FindAllCompetitions = CompetitionQueries.FindAllCompetitions
typealias FindCompetition = CompetitionQueries.FindCompetition
typealias MaybeFindCompetition = CompetitionQueries.MaybeFindCompetition
