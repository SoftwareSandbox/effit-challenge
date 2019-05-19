package be.swsb.effit.competition.competitor

import be.swsb.effit.competition.Competitor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CompetitorRepository : JpaRepository<Competitor, UUID>
