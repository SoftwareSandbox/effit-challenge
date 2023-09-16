package be.swsb.effit.backend.domain.query.competition

import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.CompetitionId
import be.swsb.effit.backend.domain.query.Query

sealed class CompetitionQueries {
    object FindAllCompetitions: Query<List<Competition>>
    data class FindCompetition(val id: CompetitionId): Query<Competition>
    data class MaybeFindCompetition(val id: CompetitionId): Query<Competition?>
}
