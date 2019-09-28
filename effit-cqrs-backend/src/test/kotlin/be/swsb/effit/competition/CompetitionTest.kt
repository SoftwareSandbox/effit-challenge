package be.swsb.effit.competition

import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.challenge.defaultChallengeForTest
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.CompetitorNotFoundOnCompetitionDomainException
import be.swsb.effit.domain.core.competition.UnableToAddCompetitorToStartedCompetitionDomainException
import be.swsb.effit.domain.core.competition.UnableToRemoveCompetitorOfAStartedCompetitionDomainException
import be.swsb.effit.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

class CompetitionTest {

    @Test
    fun `A Competition can be created with a Name, Start and End Date`() {
        val startDate = LocalDate.of(2018, 3, 19)
        val endDate = LocalDate.of(2018, 3, 29)
        val actual = Competition.competition(name = "SnowCase Val Thorens 2018", startDate = startDate, endDate = endDate)

        assertThat(actual.name).isEqualTo("SnowCase Val Thorens 2018")
        assertThat(actual.startDate).isEqualTo(startDate)
        assertThat(actual.endDate).isEqualTo(endDate)
    }

    @Test
    fun `A Competition has a CompetitionId upon creation`() {
        val startDate = LocalDate.of(2018, 3, 19)
        val actual = Competition.competitionWithoutEndDate(name = "SnowCase Val Thorens 2018", startDate = startDate)

        assertThat(actual.competitionId).isEqualTo(CompetitionId("SnowCase Val Thorens 2018"))
    }

    @Test
    fun `A Competition cannot be created with an endDate before the startDate`() {
        val startDate = LocalDate.of(2018, 3, 19)
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

        val actual = Competition.competitionWithoutStartDate(name="Something", endDate = today.plusWeeks(1))

        assertThat(actual.startDate).isEqualTo(today)
    }

    @Test
    fun `when a Competition is created with just an startDate, endDate defaults to 10 days from the startDate`() {
        val startDate = LocalDate.of(2019, 4, 9)

        val actual = Competition.competitionWithoutEndDate(name="Something", startDate = startDate)

        assertThat(actual.endDate).isEqualTo(startDate.plusDays(10))
    }

    @Test
    fun `when a Competition is created, it's not started`() {
        val actual = Competition.competitionWithoutEndDate(name="Something", startDate = LocalDate.of(2019,1,1))

        assertThat(actual.started).isFalse()
    }

    @Test
    fun `A Competition without Challenges still can have Challenges added to it`() {
        val someCompetition = Competition.competitionWithoutEndDate(name="Something", startDate = LocalDate.of(2019, 4, 9))

        val picassoChallenge = Challenge.defaultChallengeForTest()
        someCompetition.addChallenge(picassoChallenge)

        assertThat(someCompetition.challenges).contains(picassoChallenge)
    }

    @Test
    fun `A Competition without Competitors still can have Competitors added to it`() {
        val someCompetition = Competition.competitionWithoutEndDate(name="Something", startDate = LocalDate.of(2019, 4, 9))

        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        someCompetition.addCompetitor(snarf)

        assertThat(someCompetition.competitors).contains(snarf)
    }

    @Test
    fun `A Competition that is started cannot have Competitors added to it`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val someStartedCompetition = Competition.defaultStartedCompetition(competitors = listOf(snarf))

        val lionO = Competitor.defaultCompetitorForTest(name = "Lion-O")

        assertThatExceptionOfType(UnableToAddCompetitorToStartedCompetitionDomainException::class.java)
                .isThrownBy { someStartedCompetition.addCompetitor(lionO) }

        assertThat(someStartedCompetition.competitors)
                .contains(snarf)
                .doesNotContain(lionO)
    }

    @Test
    fun `removeCompetitor when no matching competitor is found, throw DomainException`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val liono = Competitor.defaultCompetitorForTest(name = "Lion-O")

        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf, liono))

        assertThatExceptionOfType(CompetitorNotFoundOnCompetitionDomainException::class.java)
                .isThrownBy { someCompetition.removeCompetitor(UUID.randomUUID()) }
    }

    @Test
    fun `removeCompetitor when no Competition is started, throw DomainException`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val liono = Competitor.defaultCompetitorForTest(name = "Lion-O")

        val someCompetition = Competition.defaultStartedCompetition(competitors = listOf(snarf, liono))

        assertThatExceptionOfType(UnableToRemoveCompetitorOfAStartedCompetitionDomainException::class.java)
                .isThrownBy { someCompetition.removeCompetitor(snarf.id) }
    }

    @Test
    fun `removeCompetitor when matching competitor is found, remove it from the Competition`() {
        val liono = Competitor.defaultCompetitorForTest(name = "Lion-O")
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")

        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf, liono))

        someCompetition.removeCompetitor(liono.id)

        assertThat(someCompetition.competitors).containsExactly(snarf)
    }

    @Test
    fun `start should throw exception when no competitors were added to the competition`() {
        val someCompetition = Competition.defaultCompetitionForTest(competitors = emptyList())

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy{ someCompetition.start() }
    }

    @Test
    fun `start should throw exception when no challenges were added to the competition`() {
        val someCompetition = Competition.defaultCompetitionForTest(
                competitors = listOf(Competitor.defaultCompetitorForTest()),
                challenges = emptyList()
        )

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy{ someCompetition.start() }
    }

    @Test
    fun `completeChallenge when competition is started, should complete the challenge of given Competitor`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val someChallenge = Challenge.defaultChallengeForTest()

        val someCompetition = Competition.defaultCompetitionForTest(
                competitors = listOf(snarf),
                challenges = listOf(someChallenge),
                started = true)

        someCompetition.completeChallenge(someChallenge,snarf.id)

        assertThat(snarf.completedChallenges).containsExactly(someChallenge)
    }

    @Test
    fun `completeChallenge when competition is not yet started, should throw an exception`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val someChallenge = Challenge.defaultChallengeForTest()

        val someCompetition = Competition.defaultCompetitionForTest(
                competitors = listOf(snarf),
                challenges = listOf(someChallenge),
                started = false)

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy{ someCompetition.completeChallenge(someChallenge, snarf.id) }

        assertThat(snarf.completedChallenges).isEmpty()
    }

    @Test
    fun `completeChallenge when challenge not found on competition, should throw exception`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val someChallengeOfAnotherCompetition = Challenge.defaultChallengeForTest()

        val someCompetition = Competition.defaultStartedCompetition(competitors = listOf(snarf))

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy{ someCompetition.completeChallenge(someChallengeOfAnotherCompetition,snarf.id) }
    }

    @Test
    fun `completeChallenge when given competitor id not found on competition, should throw exception`() {
        val someChallenge = Challenge.defaultChallengeForTest()

        val someCompetition = Competition.defaultStartedCompetition(challenges = listOf(someChallenge))

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy{ someCompetition.completeChallenge(someChallenge, UUID.randomUUID()) }
    }

    @Test
    fun `removeChallenge when given challenge id not found on competition, should throw exception`() {
        val someChallenge = Challenge.defaultChallengeForTest()

        val someCompetition = Competition.defaultStartedCompetition(challenges = listOf(someChallenge))

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy{ someCompetition.removeChallenge(UUID.randomUUID()) }
    }

    @Test
    fun `removeChallenge when given challenge id found on competition, should remove challenge`() {
        val someChallenge = Challenge.defaultChallengeForTest()
        val otherChallenge = Challenge.defaultChallengeForTest()

        val someCompetition = Competition.defaultStartedCompetition(challenges = listOf(someChallenge, otherChallenge))

        someCompetition.removeChallenge(someChallenge.id)

        assertThat(someCompetition.challenges)
                .doesNotContain(someChallenge)
                .contains(otherChallenge)
    }
}
