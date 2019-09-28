package be.swsb.effit.competition

import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.challenge.defaultChallengeForTest
import be.swsb.effit.domain.core.competition.competitor.ChallengeAlreadyCompletedDomainException
import be.swsb.effit.domain.core.competition.competitor.Competitor
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import java.util.*

class CompetitorTest {

    @Test
    fun `Completing a Challenge that already was completed, throws an exception`() {
        val competitor = Competitor(name = "Snarf")

        val someChallenge = Challenge.defaultChallengeForTest()
        competitor.completeChallenge(someChallenge)

        assertThatExceptionOfType(ChallengeAlreadyCompletedDomainException::class.java)
                .isThrownBy { competitor.completeChallenge(someChallenge) }
                .withMessageContaining("This competitor already completed the ${someChallenge.name} challenge.")
    }

    @Test
    fun `totalScore can be derived from the completedChallenges`() {
        val competitor = Competitor(name = "Snarf")

        competitor.completeChallenge(Challenge.defaultChallengeForTest(points = 4))
        competitor.completeChallenge(Challenge.defaultChallengeForTest(points = 8))

        assertThat(competitor.totalScore).isEqualTo(12)
    }

    @Test
    fun `Two Competitors are equal when their id, name AND completedChallenges are the same`() {
        val snarfId = UUID.randomUUID()
        val snarf1 = Competitor(id = snarfId, name = "Snarf")
        val snarf2 = Competitor(id = snarfId, name = "Snarf")
        val snarf3 = Competitor(id = snarfId, name = "Snarf")

        val someChallenge = Challenge.defaultChallengeForTest(points = 4)
        snarf1.completeChallenge(someChallenge)
        snarf2.completeChallenge(someChallenge)
        snarf3.completeChallenge(Challenge.defaultChallengeForTest(points = 4))

        assertThat(snarf1).isEqualTo(snarf2)
        assertThat(snarf2).isEqualTo(snarf1)
        assertThat(snarf3).isNotEqualTo(snarf1)
        assertThat(snarf3).isNotEqualTo(snarf2)
        assertThat(snarf2).isNotEqualTo(snarf3)

        assertThat(listOf(snarf1))
                .containsExactly(snarf1)
                .containsExactly(snarf2)
                .doesNotContain(snarf3)
    }

    @Test
    fun `Two Competitors are equal when their id, name AND both have empty completedChallenges`() {
        val snarfId = UUID.randomUUID()
        val snarf1 = Competitor(id = snarfId, name = "Snarf", _completedChallenges = emptyList())
        val snarf2 = Competitor(id = snarfId, name = "Snarf", _completedChallenges = emptyList())

        assertThat(snarf1).isEqualTo(snarf2)
        assertThat(snarf2).isEqualTo(snarf1)

        assertThat(listOf(snarf1))
                .containsExactly(snarf1)
                .containsExactly(snarf2)
    }

}
