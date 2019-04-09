package be.swsb.effit.competition

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureTestEntityManager
class CompetitionRepositoryIntegrationTest {

    @Autowired
    lateinit var competitionRepository: CompetitionRepository
    @Autowired
    lateinit var testEntityManager: TestEntityManager

    @Test
    fun `findAll retrieves all Competitions`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        val snowCase2019 = Competition.competition("SnowCase2019", LocalDate.of(2019, 3, 18), LocalDate.of(2019, 3, 28))
        testEntityManager.persist(snowCase2018)
        testEntityManager.persist(snowCase2019)

        val actual = competitionRepository.findAll()

        assertThat(actual).containsExactly(snowCase2018, snowCase2019)
    }

    @Test
    fun `findByName, Competition with given name exists, retrieves that exact Competition`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        val snowCase2019 = Competition.competition("SnowCase2019", LocalDate.of(2019, 3, 18), LocalDate.of(2019, 3, 28))
        testEntityManager.persist(snowCase2018)
        testEntityManager.persist(snowCase2019)

        val actual = competitionRepository.findByName("SnowCase2019")

        assertThat(actual).isEqualTo(snowCase2019)
    }

    @Test
    fun `findByName, Competition with given name does not exist, returns null`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        testEntityManager.persist(snowCase2018)

        val actual = competitionRepository.findByName("snarf")

        assertThat(actual).isNull()
    }
}