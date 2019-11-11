package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AddCompetitorCommandHandlerTest {

    @Mock
    private lateinit var queryExecutor: QueryExecutor
    @Mock
    private lateinit var competitorRepository: CompetitorRepository
    @Mock
    private lateinit var competitionRepository: CompetitionRepository
    @Captor
    private lateinit var createdCompetitorCaptor: ArgumentCaptor<Competitor>
    @Captor
    private lateinit var savedCompetitionCaptor: ArgumentCaptor<Competition>

    private lateinit var handler: AddCompetitorCommandHandler

    @BeforeEach
    fun setUp() {
        handler = AddCompetitorCommandHandler(queryExecutor, competitionRepository, competitorRepository)
    }

    @Test
    fun `handle | When Competition found, create Competitor and add it to the Competition`() {
        val thundercatsCompetition = Competition.defaultCompetitionForTest()
        val givenCompetitionId = CompetitionId("ThundercatsCompetition")
        val competitorName = CompetitorName("Snarf")

        `when`(queryExecutor.execute(FindCompetition(givenCompetitionId))).thenReturn(thundercatsCompetition)

        val savedCompetitor = Competitor.defaultCompetitorForTest(name="Snarf")
        `when`(competitorRepository.save(createdCompetitorCaptor.capture())).thenReturn(savedCompetitor)
        val updatedAndSavedCompetition = Competition.defaultCompetitionForTest()
        `when`(competitionRepository.save(savedCompetitionCaptor.capture())).thenReturn(updatedAndSavedCompetition)

        handler.handle(AddCompetitor(givenCompetitionId, competitorName))

        verify(competitorRepository).save(createdCompetitorCaptor.capture())

        val actualSavedCompetitor = createdCompetitorCaptor.value
        assertThat(actualSavedCompetitor.completedChallenges).isEmpty()
        assertThat(actualSavedCompetitor.id).isNotNull()
        assertThat(actualSavedCompetitor.name).isEqualTo("Snarf")

        assertThat(thundercatsCompetition.competitors).containsExactly(savedCompetitor)

        verify(competitionRepository).save(thundercatsCompetition)
    }
}
