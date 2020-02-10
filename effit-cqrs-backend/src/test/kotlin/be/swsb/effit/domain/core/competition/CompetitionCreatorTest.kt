package be.swsb.effit.domain.core.competition

import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CompetitionCreatorTest {

    @Test
    fun `without startDate, creates Competition with default startDate`() {
        val endDate = LocalDate.now().plusDays(5)
        val actual = CompetitionCreator().from(CreateCompetition("unimportant", null, endDate))
        assertThat(actual.startDate).isNotNull()
    }

    @Test
    fun `without endDate, creates Competition with default endDate`() {
        val startDate = LocalDate.now().minusDays(8)
        val actual = CompetitionCreator().from(CreateCompetition("unimportant", startDate, null))
        assertThat(actual.endDate).isNotNull()
    }

    @Test
    fun `with both start and endDate, creates Competition with default start and end dates`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        val actual = CompetitionCreator().from(CreateCompetition("unimportant", startDate, endDate))
        assertThat(actual.startDate).isNotNull()
        assertThat(actual.endDate).isNotNull()
    }

    @Test
    fun `Cannot create a Competition without both a start and end date`() {
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition("unimportant", null, null))
                }
                .withMessage("Cannot create a Competition without both a start and end date")
    }

    @Test
    fun `name is null, cannot create a Competition without a name`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition(null, startDate, endDate))
                }
                .withMessage("Cannot create a Competition without a name")
    }

    @Test
    fun `name is blank, cannot create a Competition without a name`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition(" ", startDate, endDate))
                }
                .withMessage("Cannot create a Competition without a name")
    }

    @Test
    fun `name contains more than 25 characters, should throw exception`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        assertThatExceptionOfType(DomainValidationRuntimeException::class.java)
                .isThrownBy {
                    CompetitionCreator().from(CreateCompetition("a".repeat(26), startDate, endDate))
                }
                .withMessage("Cannot create a Competition with a name longer than 25 characters")
    }

    @Test
    fun `name contains exactly 25 characters, should not throw exception`() {
        val startDate = LocalDate.now().minusDays(8)
        val endDate = LocalDate.now().plusDays(8)
        val nameOfExactly25Characters = "a".repeat(25)
        val createdCompetition = CompetitionCreator().from(CreateCompetition(nameOfExactly25Characters, startDate, endDate))
        assertThat(createdCompetition.name).isEqualTo(nameOfExactly25Characters)
    }
}
