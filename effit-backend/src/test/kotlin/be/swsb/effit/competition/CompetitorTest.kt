package be.swsb.effit.competition

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompetitorTest {

    @Test
    fun `awardPoints should tally the given points to the total`() {
        val competitor = Competitor(name = "Snarf", totalScore = 3)

        competitor.awardPoints(4)

        assertThat(competitor.totalScore).isEqualTo(7)
    }
}