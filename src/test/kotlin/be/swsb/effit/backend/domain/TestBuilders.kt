package be.swsb.effit.backend.domain

import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.competition.competitor.Competitor
import java.util.*
import kotlin.random.Random

fun aDefaultCompetitorForTest(
    id: UUID = UUID.randomUUID(),
    name: String = Random.nextBits(256).toString(),
    completedChallenges: List<Challenge> = emptyList()
) : Competitor {
    val competitor = Competitor(id = id, name = name)
    completedChallenges.forEach { competitor.completeChallenge(it) }
    return competitor
}
