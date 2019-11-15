package be.swsb.effit.domain.command.competition.competitor

import be.swsb.effit.adapter.ui.util.RestApiExposed
import java.util.*

data class CompetitorId(val id: UUID): RestApiExposed
