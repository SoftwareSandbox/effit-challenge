package be.swsb.effit.adapter.sql.competition

import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CompetitionRepository : JpaRepository<Competition, UUID> {
    fun findByCompetitionIdentifier(competitionId: CompetitionId): Competition?
}
