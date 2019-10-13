package be.swsb.effit.domain.query.competition

import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.query.Query

data class FindByCompetitionId(val id: CompetitionId): Query<Competition?>
