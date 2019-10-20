package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
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
class UnstartCompetitionCommandHandlerTest {

    @Mock
    private lateinit var queryExecutor: QueryExecutor
    @Mock
    private lateinit var competitionRepository: CompetitionRepository
    @Captor
    private lateinit var competitionCaptor: ArgumentCaptor<Competition>

    private lateinit var handler: UnstartCompetitionCommandHandler

    @BeforeEach
    fun setUp() {
        handler = UnstartCompetitionCommandHandler(queryExecutor, competitionRepository)
    }

    @Test
    fun `handle | when no competition found, should throw EntityNotFoundException`() {
        val competitionId = CompetitionId("Spiel2019")

        `when`(queryExecutor.execute(FindCompetition(competitionId))).thenReturn(null)

        assertThatExceptionOfType(EntityNotFoundDomainRuntimeException::class.java)
                .isThrownBy { handler.handle(UnstartCompetition(competitionId)) }
                .withMessage("Competition with id Spiel2019 not found")
    }

    @Test
    fun `handle | when competition found and already not started, should just return the competition`() {
        val competitionId = CompetitionId("Spiel2019")

        val unstartedCompetition = Competition.defaultCompetitionForTest(started = false, name = "Spiel2019")
        `when`(queryExecutor.execute(FindCompetition(competitionId))).thenReturn(unstartedCompetition)

        val actual = handler.handle(UnstartCompetition(competitionId))

        assertThat(actual).isEqualTo(unstartedCompetition)
        verify(competitionRepository, never()).save(ArgumentMatchers.any(Competition::class.java))
    }

    @Test
    fun `handle | when competition was found and started, should "unstart" it`() {
        val competitionId = CompetitionId("Spiel2019")

        val startedCompetition = Competition.defaultStartedCompetition(name = "Spiel2019")
        `when`(queryExecutor.execute(FindCompetition(competitionId))).thenReturn(startedCompetition)
        val savedCompetition = Competition.defaultCompetitionForTest(started = false, name = "Spiel2019")
        `when`(competitionRepository.save(competitionCaptor.capture())).thenReturn(savedCompetition)

        val actual = handler.handle(UnstartCompetition(competitionId))

        assertThat(actual).isEqualTo(savedCompetition)
        assertThat(competitionCaptor.value.started).isFalse()
    }
}
