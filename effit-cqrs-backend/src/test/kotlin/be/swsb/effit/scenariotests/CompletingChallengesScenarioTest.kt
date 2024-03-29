package be.swsb.effit.scenariotests

import be.swsb.effit.EffitCqrsApplication
import be.swsb.effit.adapter.ui.web.SecurityConfig
import be.swsb.effit.domain.command.competition.ChallengeToAdd
import be.swsb.effit.domain.command.competition.CreateCompetition
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.competition.competitor.defaultCompetitorForTest
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [EffitCqrsApplication::class, SecurityConfig::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CompletingChallengesScenarioTest {

    @Autowired
    private lateinit var context: WebApplicationContext
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var scenarios: Scenarios

    @BeforeEach
    fun setUp() {
        val mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()

        scenarios = Scenarios(mockMvc, objectMapper)
    }

    @Test
    fun `Snarf completes the whinge challenge and gets awarded 4 points`() {
        val whingeChallenge = ChallengeToAdd(name = "Whinge", points = 4, description = "Whinge at any point during the day")

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
}
