package be.swsb.effit.domain.query.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FindCompetitionQueryHandlerTest {

    @Mock
    private lateinit var competitionRepositoryMock: CompetitionRepository
    private lateinit var handler: FindCompetitionQueryHandler

    @BeforeEach
    fun setUp() {
        handler = FindCompetitionQueryHandler(competitionRepositoryMock)
    }

    @Test
    fun `handle | competition found return competition`() {
        val givenCompetitionId = CompetitionId("Thundercats")
        val expectedCompetition = Competition.defaultCompetitionForTest()
        `when`(competitionRepositoryMock.findByCompetitionIdentifier(givenCompetitionId)).thenReturn(expectedCompetition)

        val actual = handler.handle(FindCompetition(givenCompetitionId))

        assertThat(actual).isEqualTo(expectedCompetition)
    }

    @Test
    fun `handle | no competition found returns null`() {
        val givenCompetitionId = CompetitionId("Thundercats")
        `when`(competitionRepositoryMock.findByCompetitionIdentifier(givenCompetitionId)).thenReturn(null)

        val actual = handler.handle(FindCompetition(givenCompetitionId))

        assertThat(actual).isNull()
    }
}
