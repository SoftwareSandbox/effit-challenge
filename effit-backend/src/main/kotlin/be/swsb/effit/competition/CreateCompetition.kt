package be.swsb.effit.competition

import be.swsb.effit.util.RestApiExposed
import java.time.LocalDate

data class CreateCompetition(val name: String, val startDate: LocalDate?, val endDate: LocalDate?): RestApiExposed
