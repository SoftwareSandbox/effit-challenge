package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.command.Command
import be.swsb.effit.domain.core.competition.Competition
import java.time.LocalDate

data class CreateCompetition(val name: String?,
                             val startDate: LocalDate?,
                             val endDate: LocalDate?): RestApiExposed, Command<Competition>
