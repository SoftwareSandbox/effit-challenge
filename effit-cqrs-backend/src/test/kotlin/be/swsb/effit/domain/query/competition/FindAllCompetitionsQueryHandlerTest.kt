package be.swsb.effit.domain.query.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FindAllCompetitionsQueryHandlerTest {

    @Mock
    private lateinit var competitionRepositoryMock: CompetitionRepository

    @Test
    fun `handle | delegates to CompetitionRepository`() {
        val expected = listOf(Competition.defaultCompetitionForTest())
        `when`(competitionRepositoryMock.findAll()).thenReturn(expected)

        val actual = FindAllCompetitionsQueryHandler(competitionRepositoryMock).handle(FindAllCompetitions)

        assertThat(actual).isEqualTo(expected)
    }
}
