package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.command.competition.ChallengeToAdd
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.command.competition.competitor.randomCompetitorName
import be.swsb.effit.domain.command.competition.defaultChallengeToAddForTest
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.competition.competitor.Competitor
import java.time.LocalDate

fun Competition.Companion.defaultCompetitionForTest(
        name: String = "Default Competition",
        startDate: LocalDate = LocalDate.of(2019, 4, 1),
        endDate: LocalDate = LocalDate.of(2019, 4, 11),
        competitors: List<CompetitorName> = emptyList(),
        challenges: List<ChallengeToAdd> = emptyList(),
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
        competitors: List<CompetitorName> = listOf(CompetitorName.randomCompetitorName()),
        challenges: List<ChallengeToAdd> = listOf(ChallengeToAdd.defaultChallengeToAddForTest()))
        : Competition {
    return Competition.defaultCompetitionForTest(name, startDate, endDate, competitors, challenges, true)
}

fun Competition.Companion.defaultCompetitionWithChallengesAndCompetitorsForTest(
        name: String = "Default Competition",
        startDate: LocalDate = LocalDate.of(2019, 4, 1),
        endDate: LocalDate = LocalDate.of(2019, 4, 11),
        competitors: List<CompetitorName> = listOf(CompetitorName.randomCompetitorName()),
        challenges: List<ChallengeToAdd> = listOf(ChallengeToAdd.defaultChallengeToAddForTest()),
        started: Boolean = false)
        : Competition {
    return Competition.defaultCompetitionForTest(name, startDate, endDate, competitors, challenges, started)
}

fun Competition.findCompetitor(name: String) : Competitor {
    return this.competitors.find { it.name == name } ?: throw IllegalStateException("Expected competition to have Competitor with name $name")
}

fun Competition.findChallenge(name: String) : Challenge {
    return this.challenges.find { it.name == name } ?: throw IllegalStateException("Expected competition to have Challenge with name $name")
}
