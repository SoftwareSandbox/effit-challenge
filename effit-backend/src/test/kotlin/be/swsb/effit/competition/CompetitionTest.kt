package be.swsb.effit.competition

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CompetitionTest {

    @Test
    fun `when a Competition is created with just an endDate, startDate defaults to today`() {
        val today = LocalDate.now()

        val actual = Competition.competitionWithoutStartDate(endDate = today.plusWeeks(1))

        assertThat(actual.startDate).isEqualTo(today)
    }

    @Test
    fun `when a Competition is created with just an startDate, endDate defaults to 10 days from the startDate`() {
        val startDate = LocalDate.of(2019,4,9)

        val actual = Competition.competitionWithoutEndDate(startDate = startDate)

        assertThat(actual.endDate).isEqualTo(startDate.plusDays(10))
    }
}