package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CompetitionTest {

    @Test
    fun `A Competition can be created with a Name, Start and End Date`() {
        val startDate = LocalDate.of(2018,3,19)
        val endDate = LocalDate.of(2018,3,29)
        val actual = Competition.competition(name = "SnowCase Val Thorens 2018", startDate = startDate, endDate = endDate)

        assertThat(actual.name).isEqualTo("SnowCase Val Thorens 2018")
        assertThat(actual.startDate).isEqualTo(startDate)
        assertThat(actual.endDate).isEqualTo(endDate)
    }

    @Test
    fun `A Competition has a CompetitionId upon creation`() {
        val startDate = LocalDate.of(2018,3,19)
        val actual = Competition.competitionWithoutEndDate(name = "SnowCase Val Thorens 2018", startDate = startDate)

        assertThat(actual.competitionId).isEqualTo(CompetitionId("SnowCase Val Thorens 2018"))
    }

    @Test
    fun `A Competition cannot be created with an endDate before the startDate`() {
        val startDate = LocalDate.of(2018,3,19)
        val endDate = startDate.minusDays(10)

        assertThatThrownBy {
            Competition.competition(name = "SnowCase Val Thorens 2018", startDate = startDate, endDate = endDate)
        }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("The end date can not be before the start date")
    }

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

    @Test
    fun `a Competition without Challenges still can have Challenges added to it`() {
        val someCompetition = Competition.competitionWithoutEndDate(startDate = LocalDate.of(2019,4,9))

        val picassoChallenge = Challenge(name = "Picasso", points = 3, description = "description")
        someCompetition.addChallenge(picassoChallenge)

        assertThat(someCompetition.challenges).contains(picassoChallenge)
    }
}