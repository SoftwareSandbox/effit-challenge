package be.swsb.effit.domain.command.competition.competitor

import be.swsb.test.effit.RandomTestUtil.Companion.randomString

fun CompetitorName.Companion.randomCompetitorName(): CompetitorName {
    return CompetitorName(randomString(50))
}
