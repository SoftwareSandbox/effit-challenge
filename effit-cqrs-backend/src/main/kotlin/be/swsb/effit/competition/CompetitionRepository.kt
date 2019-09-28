package be.swsb.effit.competition

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CompetitionRepository : JpaRepository<Competition, UUID> {
    fun findByCompetitionIdentifier(competitionId: CompetitionId): Competition?
}