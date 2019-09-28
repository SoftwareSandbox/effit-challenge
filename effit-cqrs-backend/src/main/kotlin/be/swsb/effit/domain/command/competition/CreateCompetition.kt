package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.ui.util.RestApiExposed
import java.time.LocalDate

data class CreateCompetition(val name: String?,
                             val startDate: LocalDate?,
                             val endDate: LocalDate?): RestApiExposed
