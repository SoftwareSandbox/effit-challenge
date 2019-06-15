package be.swsb.effit.competition

import be.swsb.effit.competition.competitor.Competitor
import java.time.LocalDate

fun Competition.Companion.defaultCompetitionForTest(
        name: String = "Default Competition",
        startDate: LocalDate = LocalDate.of(2019, 4, 1),
        endDate: LocalDate = LocalDate.of(2019, 4, 11),
        competitors: List<Competitor> = emptyList())
        : Competition {
    val competition = competition(name = name, startDate = startDate, endDate = endDate)
    competitors.forEach { competition.addCompetitor(it) }
    return competition
}