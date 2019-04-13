package be.swsb.effit

import be.swsb.effit.challenge.ChallengeRepository
import be.swsb.effit.competition.CompetitionRepository
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(resourcePattern = "**/*Controller.class")
class WebMvcTestConfiguration {
    @MockBean
    lateinit var competitionRepository: CompetitionRepository

    @MockBean
    lateinit var challengeRepository: ChallengeRepository
}