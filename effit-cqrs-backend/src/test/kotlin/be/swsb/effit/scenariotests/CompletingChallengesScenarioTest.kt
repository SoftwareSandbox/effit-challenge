package be.swsb.effit.scenariotests

import be.swsb.effit.EffitApplication
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.exceptions.EntityNotFoundDomainRuntimeException
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [EffitApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CompletingChallengesScenarioTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    private lateinit var scenarios: Scenarios

    @BeforeEach
    fun setUp() {
        scenarios = Scenarios(mockMvc, objectMapper)
    }

    @Test
    fun `Snarf completes the whinge challenge and gets awarded 4 points`() {
        val whingeChallenge = Challenge(name = "Whinge", points = 4, description = "Whinge at any point during the day")

        val competition = CreateCompetition(name = "ThundercatsCompo 2019",
                startDate = LocalDate.of(2018, 3, 16),
                endDate = LocalDate.of(2018, 3, 26))
        val competitionId = scenarios.createNewCompetition(competition)

        scenarios.addChallenges(competitionId, listOf(whingeChallenge))

        val snarfId = scenarios.addCompetitor("Snarf", competitionId)

        val fetchedWhingeChallenge = scenarios.getCompetition(competitionId).challenges
                .find { it.name == whingeChallenge.name }
                ?: throw EntityNotFoundDomainRuntimeException("Challenge ${whingeChallenge.name} not found in competition $competitionId")

        scenarios.startCompetition(competitionId)

        scenarios.completeChallenge(competitionId, fetchedWhingeChallenge.id, snarfId)

        val updatedThundercatsCompetition = scenarios.getCompetition(competitionId)

        val snarfWithCompletedChallenge = Competitor.defaultCompetitorForTest(id = snarfId, name = "Snarf")
        snarfWithCompletedChallenge.completeChallenge(fetchedWhingeChallenge)

        assertThat(updatedThundercatsCompetition.competitors).containsExactly(snarfWithCompletedChallenge)
    }

    @Test
    fun `LionO becomes the leader of a competition, is removed by a salty admin, causing Snarf who was second to become the leader`() {
        //TODO implement this at some point
    }
}
