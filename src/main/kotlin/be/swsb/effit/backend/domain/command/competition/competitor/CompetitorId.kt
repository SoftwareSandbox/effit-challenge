package be.swsb.effit.backend.domain.command.competition.competitor

import be.swsb.effit.backend.domain.RestApiExposed
import java.util.*

data class CompetitorId(val id: UUID): RestApiExposed
