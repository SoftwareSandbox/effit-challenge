package be.swsb.effit.domain.command.competition.competitor

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.command.competition.AddCompetitor
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.competitor.Competitor
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AddCompetitorCommandHandlerTest {

    @Mock
    private lateinit var queryExecutor: QueryExecutor
    @Mock
    private lateinit var competitionRepository: CompetitionRepository
    @Captor
    private lateinit var savedCompetitionCaptor: ArgumentCaptor<Competition>

    private lateinit var handler: AddCompetitorCommandHandler

    @BeforeEach
    fun setUp() {
        handler = AddCompetitorCommandHandler(queryExecutor, competitionRepository)
    }

    @Test
    fun `handle | When Competition found, create Competitor and add it to the Competition`() {
        val thundercatsCompetition = Competition.defaultCompetitionForTest()
        val givenCompetitionId = CompetitionId("ThundercatsCompetition")
        val competitorName = CompetitorName("Snarf")

        `when`(queryExecutor.execute(FindCompetition(givenCompetitionId))).thenReturn(thundercatsCompetition)

        val savedCompetitor = Competitor.defaultCompetitorForTest(name="Snarf")
        val updatedAndSavedCompetition = Competition.defaultCompetitionForTest()
        `when`(competitionRepository.save(savedCompetitionCaptor.capture())).thenReturn(updatedAndSavedCompetition)

        handler.handle(AddCompetitor(givenCompetitionId, competitorName))

        assertThat(thundercatsCompetition.competitors)
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(savedCompetitor)

        verify(competitionRepository).save(thundercatsCompetition)
    }
}
