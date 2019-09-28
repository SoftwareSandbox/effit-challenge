package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.challenge.defaultChallengeForTest
import be.swsb.effit.competition.competitor.Competitor
import be.swsb.effit.competition.competitor.defaultCompetitorForTest
import java.time.LocalDate

fun Competition.Companion.defaultCompetitionForTest(
        name: String = "Default Competition",
        startDate: LocalDate = LocalDate.of(2019, 4, 1),
        endDate: LocalDate = LocalDate.of(2019, 4, 11),
        competitors: List<Competitor> = emptyList(),
        challenges: List<Challenge> = emptyList(),
        started: Boolean = false)
        : Competition {
    val competition = competition(name = name, startDate = startDate, endDate = endDate)
    competitors.forEach { competition.addCompetitor(it) }
    challenges.forEach { competition.addChallenge(it) }
    if (started) {
        competition.start()
    }
    return competition
}

fun Competition.Companion.defaultStartedCompetition(
        name: String = "Default Started Competition",
        startDate: LocalDate = LocalDate.of(2019, 4, 1),
        endDate: LocalDate = LocalDate.of(2019, 4, 11),
        competitors: List<Competitor> = listOf(Competitor.defaultCompetitorForTest()),
        challenges: List<Challenge> = listOf(Challenge.defaultChallengeForTest()))
        : Competition {
    return Competition.defaultCompetitionForTest(name, startDate, endDate, competitors, challenges, true)
}

fun Competition.Companion.defaultCompetitionWithChallengesAndCompetitorsForTest(
        name: String = "Default Competition",
        startDate: LocalDate = LocalDate.of(2019, 4, 1),
        endDate: LocalDate = LocalDate.of(2019, 4, 11),
        competitors: List<Competitor> = listOf(Competitor.defaultCompetitorForTest()),
        challenges: List<Challenge> = listOf(Challenge.defaultChallengeForTest()),
        started: Boolean = false)
        : Competition {
    return Competition.defaultCompetitionForTest(name, startDate, endDate, competitors, challenges, started)
}