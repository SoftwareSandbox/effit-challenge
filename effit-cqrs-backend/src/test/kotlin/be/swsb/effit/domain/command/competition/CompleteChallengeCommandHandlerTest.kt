package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.ui.competition.competitor.CompleterId
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.competition.*
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.domain.query.competition.FindCompetition
import be.swsb.effit.messaging.query.QueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CompleteChallengeCommandHandlerTest {

    @Mock
    private lateinit var competitionRepositoryMock: CompetitionRepository
    @Mock
    private lateinit var queryExecutor: QueryExecutor

    private lateinit var completeChallengeCommandHandler: CompleteChallengeCommandHandler

    @BeforeEach
    fun setUp() {
        completeChallengeCommandHandler = CompleteChallengeCommandHandler(queryExecutor, competitionRepositoryMock)
    }

    @Test
    fun `handle | should add challenge given Competitors completedChallenges`() {
        val givenCompetitionId = "SnowCase2018"

        val compWithChallenges = Competition.defaultCompetitionForTest(
                name = givenCompetitionId,
                competitors = listOf(CompetitorName("Snarf")),
                challenges = listOf(ChallengeToAdd.defaultChallengeToAddForTest(name = "Picasso")),
                started = true)
        val snarf = compWithChallenges.findCompetitor("Snarf")
        val successfulCompetitor = CompleterId(snarf.id)
        val picassoChallenge = compWithChallenges.findChallenge("Picasso")
        val givenChallengeId = picassoChallenge.id
        `when findByCompetitionIdentifier then return`(givenCompetitionId, compWithChallenges)

        `when`(competitionRepositoryMock.save(any(Competition::class.java))).thenReturn(compWithChallenges)
        completeChallengeCommandHandler.handle(CompleteChallenge(CompetitionId(givenCompetitionId), givenChallengeId, successfulCompetitor))

        assertThat(snarf.completedChallenges).containsExactly(picassoChallenge)
        verify(competitionRepositoryMock).save(compWithChallenges)
    }

    @Test
    fun `handle | should throw EntityNotFound when no challenge was found`() {
        val successfulCompetitor = CompleterId(UUID.randomUUID())
        val givenCompetitionId = "SnowCase2018"
        val givenChallengeId = UUID.randomUUID()

        val compWithoutChallenges = Competition.defaultCompetitionForTest(name = givenCompetitionId)
        `when findByCompetitionIdentifier then return`(givenCompetitionId, compWithoutChallenges)

        assertThatExceptionOfType(EntityNotFoundDomainRuntimeException::class.java)
                .isThrownBy {
                    completeChallengeCommandHandler.handle(CompleteChallenge(CompetitionId(givenCompetitionId), givenChallengeId, successfulCompetitor))
                }
                .withMessage("Competition with id $givenCompetitionId has no challenge with id $givenChallengeId")
    }

    private fun `when findByCompetitionIdentifier then return`(givenCompetitionId: String, compWithChallenges: Competition) {
        `when`(queryExecutor.execute(FindCompetition(CompetitionId(givenCompetitionId)))).thenReturn(compWithChallenges)
    }
}
