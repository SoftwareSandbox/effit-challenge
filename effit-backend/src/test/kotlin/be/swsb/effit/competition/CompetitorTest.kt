package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.bytebuddy.utility.RandomString
import org.junit.jupiter.api.Test

class CompetitorTest {

    @Test
    fun `totalScore can be derived from the completedChallenges`() {
        val competitor = Competitor(name = "Snarf")

        competitor.completeChallenge(challengeWithPoints(4))
        competitor.completeChallenge(challengeWithPoints(8))

        assertThat(competitor.totalScore).isEqualTo(12)
    }

    private fun challengeWithPoints(points: Int): Challenge {
        return Challenge(points = points, name = RandomString.make(), description = RandomString.make())
    }
}