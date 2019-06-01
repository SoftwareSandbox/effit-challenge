package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.bytebuddy.utility.RandomString
import org.junit.jupiter.api.Test
import java.util.*

class CompetitorTest {

    @Test
    fun `totalScore can be derived from the completedChallenges`() {
        val competitor = Competitor(name = "Snarf")

        competitor.completeChallenge(challengeWithPoints(4))
        competitor.completeChallenge(challengeWithPoints(8))

        assertThat(competitor.totalScore).isEqualTo(12)
    }

    @Test
    fun `Two Competitors are equal when their id, name AND completedChallenges are the same`() {
        val snarfId = UUID.randomUUID()
        val snarf1 = Competitor(id = snarfId, name = "Snarf")
        val snarf2 = Competitor(id = snarfId, name = "Snarf")
        val snarf3 = Competitor(id = snarfId, name = "Snarf")

        val someChallenge = challengeWithPoints(4)
        snarf1.completeChallenge(someChallenge)
        snarf2.completeChallenge(someChallenge)
        snarf3.completeChallenge(challengeWithPoints(4))

        assertThat(snarf1).isEqualTo(snarf2)
        assertThat(snarf2).isEqualTo(snarf1)
        assertThat(snarf3).isNotEqualTo(snarf1)
        assertThat(snarf3).isNotEqualTo(snarf2)
        assertThat(snarf2).isNotEqualTo(snarf3)
    }

    private fun challengeWithPoints(points: Int): Challenge {
        return Challenge(points = points, name = RandomString.make(), description = RandomString.make())
    }
}