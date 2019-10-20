package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.defaultCompetitionWithChallengesAndCompetitorsForTest
import be.swsb.effit.domain.core.competition.defaultStartedCompetition
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class StartCompetitionCommandHandlerTest {

    @Mock
    private lateinit var competitionRepositoryMock: CompetitionRepository
    @Mock
    private lateinit var queryExecutorMock: QueryExecutor
    @Captor
    private lateinit var competitionCaptor: ArgumentCaptor<Competition>

    private lateinit var handler: StartCompetitionCommandHandler

    @BeforeEach
    fun setUp() {
        handler = StartCompetitionCommandHandler(queryExecutorMock, competitionRepositoryMock)
    }

    @Test
    fun `handle | when competition found and not yet started, should start the competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingCompetition = Competition.defaultCompetitionWithChallengesAndCompetitorsForTest(name = competitionId)
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(competitionId)))).thenReturn(existingCompetition)
        `when`(competitionRepositoryMock.save(competitionCaptor.capture())).thenReturn(existingCompetition)

        handler.handle(StartCompetition(CompetitionId(competitionId)))

        val savedCompetition = competitionCaptor.value
        assertThat(savedCompetition.started).isTrue()
    }

    @Test
    fun `handle | when competition found and already started, should just return the competition`() {
        val competitionId = "ThundercatsCompetition"
        val existingStartedCompetition = Competition.defaultStartedCompetition(name = competitionId)
        `when`(queryExecutorMock.execute(FindCompetition(CompetitionId(competitionId)))).thenReturn(existingStartedCompetition)

        handler.handle(StartCompetition(CompetitionId(competitionId)))

        verify(competitionRepositoryMock, never()).save(ArgumentMatchers.any(Competition::class.java))
    }
}
