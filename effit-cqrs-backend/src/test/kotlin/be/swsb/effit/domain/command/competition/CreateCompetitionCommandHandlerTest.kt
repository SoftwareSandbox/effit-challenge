package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.*
import be.swsb.effit.domain.query.competition.MaybeFindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class CreateCompetitionCommandHandlerTest {
    @Mock
    private lateinit var competitionCreatorMock: CompetitionCreator
    @Mock
    private lateinit var queryExecutorMock: QueryExecutor
    @Mock
    private lateinit var competitionRepositoryMock: CompetitionRepository

    private lateinit var handler: CreateCompetitionCommandHandler

    @BeforeEach
    fun setUp() {
        handler = CreateCompetitionCommandHandler(queryExecutorMock, competitionCreatorMock, competitionRepositoryMock)
    }

    @Test
    fun `handle | should be able to create a new Competition and save it`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        val competitionToCreate = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val createdCompetition = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        `when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        `when`(queryExecutorMock.execute(MaybeFindCompetition(CompetitionId("Snowcase 2018")))).thenReturn(null)
        `when`(competitionRepositoryMock.save(ArgumentMatchers.any(Competition::class.java))).thenReturn(createdCompetition)

        val actual = handler.handle(createCompetition)

        assertThat(actual).isEqualTo(createdCompetition)
    }

    @Test
    fun `should throw CompetitionAlreadyExistsException when CompetitionId already exists`() {
        val createCompetition = CreateCompetition(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))
        val competitionToCreate = Competition.defaultCompetitionForTest(name = "Snowcase 2018",
                startDate = LocalDate.of(2018, 3, 15),
                endDate = LocalDate.of(2018, 3, 25))

        val competitionIdThatAlreadyExists = competitionToCreate.competitionId

        `when`(competitionCreatorMock.from(createCompetition)).thenReturn(competitionToCreate)
        `when`(queryExecutorMock.execute(MaybeFindCompetition(competitionIdThatAlreadyExists))).thenReturn(competitionToCreate)

        assertThatExceptionOfType(CompetitionAlreadyExistsDomainException::class.java)
                .isThrownBy { handler.handle(createCompetition) }
                .withMessageContaining(competitionIdThatAlreadyExists.id)
    }
}
