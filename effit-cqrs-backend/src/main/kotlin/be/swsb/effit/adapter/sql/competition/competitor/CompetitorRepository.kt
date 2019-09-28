package be.swsb.effit.adapter.sql.competition.competitor

import be.swsb.effit.domain.core.competition.competitor.Competitor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CompetitorRepository : JpaRepository<Competitor, UUID>
