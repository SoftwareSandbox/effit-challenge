package be.swsb.test.effit

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.sql.competition.CompetitionRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.JpaRepository

@Configuration
@ComponentScan(basePackages = ["be.swsb.effit"],
        excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes=[JpaRepository::class])])
class WebMvcTestConfiguration {
    @MockBean
    lateinit var competitionRepository: CompetitionRepository
    @MockBean
    lateinit var competitorRepository: CompetitorRepository
    @MockBean
    lateinit var challengeRepository: ChallengeRepository
}
