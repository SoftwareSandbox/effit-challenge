package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.command.competition.ChallengeToAdd
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.command.competition.competitor.randomCompetitorName
import be.swsb.effit.domain.command.competition.defaultChallengeToAddForTest
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
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

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { Competition.competition(name = "SnowCase Val Thorens 2018", startDate = startDate, endDate = endDate) }
                .withMessage("The end date can not be before the start date")
    }

    @Test
    fun `when a Competition is created with just an endDate, startDate defaults to today`() {
        val today = LocalDate.now()

        val actual = Competition.competitionWithoutStartDate(name = "Something", endDate = today.plusWeeks(1))

        assertThat(actual.startDate).isEqualTo(today)
    }

    @Test
    fun `when a Competition is created with just an startDate, endDate defaults to 10 days from the startDate`() {
        val startDate = LocalDate.of(2019, 4, 9)

        val actual = Competition.competitionWithoutEndDate(name = "Something", startDate = startDate)

        assertThat(actual.endDate).isEqualTo(startDate.plusDays(10))
    }

    @Test
    fun `when a Competition is created, it's not started`() {
        val actual = Competition.competitionWithoutEndDate(name = "Something", startDate = LocalDate.of(2019, 1, 1))

        assertThat(actual.started).isFalse()
    }

    @Test
    fun `addCompetitor | A Competition without Competitors still can have Competitors added to it`() {
        val someCompetition = Competition.competitionWithoutEndDate(name = "Something", startDate = LocalDate.of(2019, 4, 9))

        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        someCompetition.addCompetitor(CompetitorName("Snarf"))

        assertThat(someCompetition.competitors)
                .usingElementComparatorIgnoringFields("id")
                .contains(snarf)
    }

    @Test
    fun `addCompetitor | A Competition that is started cannot have Competitors added to it`() {
        val snarf = Competitor.defaultCompetitorForTest(name = "Snarf")
        val someStartedCompetition = Competition.defaultStartedCompetition(competitors = listOf(CompetitorName("Snarf")))

        val lionO = Competitor.defaultCompetitorForTest(name = "Lion-O")

        assertThatExceptionOfType(UnableToAddCompetitorToStartedCompetitionDomainException::class.java)
                .isThrownBy { someStartedCompetition.addCompetitor(CompetitorName("Lion-O")) }

        assertThat(someStartedCompetition.competitors)
                .usingElementComparatorIgnoringFields("id")
                .contains(snarf)
                .doesNotContain(lionO)
    }

    @Test
    fun `removeCompetitor | when no matching competitor is found, throw DomainException`() {
        val snarf = CompetitorName("Snarf")
        val liono = CompetitorName("Lion-O")

        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf, liono))

        assertThatExceptionOfType(CompetitorNotFoundOnCompetitionDomainException::class.java)
                .isThrownBy { someCompetition.removeCompetitor(UUID.randomUUID()) }
    }

    @Test
    fun `removeCompetitor | A Competition that is already started cannot have Competitors removed from it`() {
        val snarf = CompetitorName(name = "Snarf")
        val liono = CompetitorName(name = "Lion-O")

        val someCompetition = Competition.defaultStartedCompetition(competitors = listOf(snarf, liono))

        assertThatExceptionOfType(UnableToRemoveCompetitorOfAStartedCompetitionDomainException::class.java)
                .isThrownBy { someCompetition.removeCompetitor(UUID.randomUUID()) }
    }

    @Test
    fun `removeCompetitor | when matching competitor is found, remove it from the Competition`() {
        val lionoName = CompetitorName(name = "Lion-O")
        val snarf = CompetitorName(name = "Snarf")

        val someCompetition = Competition.defaultCompetitionForTest(competitors = listOf(snarf, lionoName))
        val liono = someCompetition.findCompetitor("Lion-O")
        someCompetition.removeCompetitor(liono.id)

        assertThat(someCompetition.competitors)
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(Competitor.defaultCompetitorForTest(name = "Snarf"))
    }

    @Test
    fun `start | should throw exception when no competitors were added to the competition`() {
        val someCompetition = Competition.defaultCompetitionForTest(competitors = emptyList())

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { someCompetition.start() }
    }

    @Test
    fun `start | should throw exception when no challenges were added to the competition`() {
        val someCompetition = Competition.defaultCompetitionForTest(
                competitors = listOf(CompetitorName.randomCompetitorName()),
                challenges = emptyList()
        )

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { someCompetition.start() }
    }

    @Test
    fun `addChallenge | A Competition without Challenges still can have Challenges added to it`() {
        val someCompetition = Competition.competitionWithoutEndDate(name = "Something", startDate = LocalDate.of(2019, 4, 9))

        val picassoChallenge = ChallengeToAdd.defaultChallengeToAddForTest(
                name = "Picasso",
                points = 4,
                description = "Some description about the Picasso Challenge"
        )
        someCompetition.addChallenge(picassoChallenge)

        assertThat(someCompetition.challenges)
                .usingElementComparatorIgnoringFields("id")
                .contains(Challenge.defaultChallengeForTest(
                        name = "Picasso",
                        points = 4,
                        description = "Some description about the Picasso Challenge"
                ))
    }

    @Test
    fun `addChallenge | A Competition that's already started cannot have Challenges added to it`() {
        val someCompetition = Competition.defaultStartedCompetition(
                name = "Something",
                challenges = listOf(ChallengeToAdd.defaultChallengeToAddForTest("Picasso"))
        )
        val existingChallenge = someCompetition.findChallenge("Picasso")

        val picassoChallenge = ChallengeToAdd.defaultChallengeToAddForTest()

        assertThatExceptionOfType(UnableToAddChallengeToStartedCompetitionDomainException::class.java)
                .isThrownBy { someCompetition.addChallenge(picassoChallenge) }

        assertThat(someCompetition.challenges).containsExactly(existingChallenge)
    }

    @Test
    fun `removeChallenge | when given challenge id not found on competition, should throw exception`() {
        val someChallenge = ChallengeToAdd.defaultChallengeToAddForTest()

        val someCompetition = Competition.defaultCompetitionForTest(challenges = listOf(someChallenge))

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { someCompetition.removeChallenge(UUID.randomUUID()) }
    }

    @Test
    fun `removeChallenge | when given challenge id found on competition, should remove challenge`() {
        val someChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeRemoved")
        val otherChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")

        val someCompetition = Competition.defaultCompetitionForTest(challenges = listOf(someChallengeToAdd, otherChallengeToAdd))
        val someChallenge = someCompetition.findChallenge("ChallengeToBeRemoved")
        val otherChallenge = someCompetition.findChallenge("Picasso")

        someCompetition.removeChallenge(someChallenge.id)

        assertThat(someCompetition.challenges)
                .doesNotContain(someChallenge)
                .contains(otherChallenge)
    }

    @Test
    fun `removeChallenge | a Competition that's already started, cannot have Challenges removed from it`() {
        val someChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeRemoved")
        val otherChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")

        val someCompetition = Competition.defaultStartedCompetition(challenges = listOf(someChallengeToAdd, otherChallengeToAdd))
        val someChallenge = someCompetition.findChallenge("ChallengeToBeRemoved")
        val otherChallenge = someCompetition.findChallenge("Picasso")

        assertThatExceptionOfType(UnableToRemoveChallengeFromStartedCompetitionDomainException::class.java)
                .isThrownBy { someCompetition.removeChallenge(someChallenge.id) }

        assertThat(someCompetition.challenges).containsExactly(someChallenge, otherChallenge)
    }

    @Test
    fun `completeChallenge | when a Competition is started, should complete the challenge of given Competitor`() {
        val snarfName = CompetitorName(name = "Snarf")
        val someChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeCompleted")

        val someCompetition = Competition.defaultCompetitionForTest(
                competitors = listOf(snarfName),
                challenges = listOf(someChallengeToAdd),
                started = true)
        val someChallenge = someCompetition.findChallenge("ChallengeToBeCompleted")

        val snarf = someCompetition.findCompetitor("Snarf")
        someCompetition.completeChallenge(someChallenge, snarf.id)

        assertThat(snarf.completedChallenges).containsExactly(someChallenge)
    }

    @Test
    fun `completeChallenge | when a Competition is not yet started, should throw an exception`() {
        val snarfName = CompetitorName(name = "Snarf")
        val someChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeCompleted")

        val someCompetition = Competition.defaultCompetitionForTest(
                competitors = listOf(snarfName),
                challenges = listOf(someChallengeToAdd),
                started = false)
        val someChallenge = someCompetition.findChallenge("ChallengeToBeCompleted")

        val snarf = someCompetition.findCompetitor("Snarf")

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { someCompetition.completeChallenge(someChallenge, snarf.id) }

        assertThat(snarf.completedChallenges).isEmpty()
    }

    @Test
    fun `completeChallenge | when challenge not found on competition, should throw exception`() {
        val snarfName = CompetitorName("Snarf")
        val someChallengeOfAnotherCompetition = Challenge.defaultChallengeForTest(name="ChallengeToBeCompleted")

        val someCompetition = Competition.defaultStartedCompetition(competitors = listOf(snarfName))
        val snarf = someCompetition.findCompetitor("Snarf")

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { someCompetition.completeChallenge(someChallengeOfAnotherCompetition, snarf.id) }
    }

    @Test
    fun `completeChallenge | when given competitor id not found on competition, should throw exception`() {
        val someChallengeToAdd = ChallengeToAdd.defaultChallengeToAddForTest(name = "ChallengeToBeCompleted")

        val someCompetition = Competition.defaultStartedCompetition(challenges = listOf(someChallengeToAdd))
        val someChallenge = someCompetition.findChallenge("ChallengeToBeCompleted")

        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy { someCompetition.completeChallenge(someChallenge, UUID.randomUUID()) }
    }
}
