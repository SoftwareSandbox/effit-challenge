package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.hibernate.exception.ConstraintViolationException
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
    fun `findByCompetitionId, Competition with given CompetitionId exists, retrieves that exact Competition`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        val snowCase2019 = Competition.competition("SnowCase2019", LocalDate.of(2019, 3, 18), LocalDate.of(2019, 3, 28))
        testEntityManager.persist(snowCase2018)
        testEntityManager.persist(snowCase2019)

        val actual = competitionRepository.findByCompetitionIdentifier(snowCase2019.competitionId)

        assertThat(actual).isEqualToComparingFieldByField(snowCase2019)
    }

    @Test
    fun `findByCompetitionId, Competition with given CompetitionId does not exist, returns null`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        testEntityManager.persist(snowCase2018)

        val actual = competitionRepository.findByCompetitionIdentifier(CompetitionId("snarf"))

        assertThat(actual).isNull()
    }

    @Test
    fun `save does not cascade Challenges of a Competition`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        testEntityManager.persist(snowCase2018)

        val existingCompetition = competitionRepository.findByCompetitionIdentifier(CompetitionId("SnowCase2018"))!!

        val someUnpersistedChallenge = Challenge(name = "Picasso", points = 3, description = "picasso yo")
        assertThat(testEntityManager.entityManager.find(Challenge::class.java, someUnpersistedChallenge.id)).isNull()

        existingCompetition.addChallenge(someUnpersistedChallenge)

        assertThatThrownBy {
            competitionRepository.save(existingCompetition)
        }
        testEntityManager.clear()

        val updatedCompetition = competitionRepository.findByCompetitionIdentifier(CompetitionId("SnowCase2018"))!!
        assertThat(updatedCompetition.challenges).doesNotContain(someUnpersistedChallenge)

    }

    @Test
    fun `saving a Competition with persisted Challenges`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        testEntityManager.persist(snowCase2018)

        val existingCompetition = competitionRepository.findByCompetitionIdentifier(CompetitionId("SnowCase2018"))!!

        val someChallenge = Challenge(name = "Picasso", points = 3, description = "picasso yo")
        testEntityManager.persist(someChallenge)
        testEntityManager.flush()

        existingCompetition.addChallenge(someChallenge)

        competitionRepository.save(existingCompetition)
        testEntityManager.flush()

        val updatedCompetition = competitionRepository.findByCompetitionIdentifier(CompetitionId("SnowCase2018"))!!
        assertThat(updatedCompetition.challenges).containsExactly(someChallenge)
    }

    @Test
    fun `Uniqueness on CompetitionId`() {
        val snowCase2018 = Competition.competition("SnowCase2018", LocalDate.of(2018, 3, 19), LocalDate.of(2018, 3, 29))
        testEntityManager.persist(snowCase2018)

        val competitionWithSameCompetitionId = Competition.competition("SnowCase2018", LocalDate.of(2019, 3, 19), LocalDate.of(2019, 3, 29))
        assertThatThrownBy {
            competitionRepository.save(competitionWithSameCompetitionId)
            testEntityManager.flush()
        }.hasCauseInstanceOf(ConstraintViolationException::class.java)
    }
}