package be.swsb.effit.adapter.sql.competition

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.adapter.sql.competition.competitor.CompetitorRepository
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.challenge.defaultChallengeForTest
import be.swsb.effit.domain.core.competition.Competition
import be.swsb.effit.domain.core.competition.CompetitionId
import be.swsb.effit.domain.core.competition.defaultCompetitionForTest
import be.swsb.effit.domain.core.competition.findCompetitor
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.hibernate.exception.ConstraintViolationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureTestEntityManager
class CompetitionRepositoryIntegrationTest {

    @Autowired
    lateinit var competitionRepository: CompetitionRepository
    @Autowired
    lateinit var competitorRepository: CompetitorRepository
    @Autowired
    lateinit var challengeRepository: ChallengeRepository
    @Autowired
    lateinit var testEntityManager: TestEntityManager

    @Test
    fun `findAll retrieves all Competitions`() {
        val snowCase2018 = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        val snowCase2019 = Competition.defaultCompetitionForTest(name = "SnowCase2019")
        testEntityManager.persist(snowCase2018)
        testEntityManager.persist(snowCase2019)

        val actual = competitionRepository.findAll()

        assertThat(actual).containsExactly(snowCase2018, snowCase2019)
    }

    @Test
    fun `findByCompetitionId, Competition with given CompetitionId exists, retrieves that exact Competition`() {
        val snowCase2018 = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        val snowCase2019 = Competition.defaultCompetitionForTest(name = "SnowCase2019")
        testEntityManager.persist(snowCase2018)
        testEntityManager.persist(snowCase2019)

        val actual = competitionRepository.findByCompetitionIdentifier(snowCase2019.competitionId)

        assertThat(actual).isEqualToComparingFieldByField(snowCase2019)
    }

    @Test
    fun `findByCompetitionId, Competition with given CompetitionId does not exist, returns null`() {
        val snowCase2018 = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        testEntityManager.persist(snowCase2018)

        val actual = competitionRepository.findByCompetitionIdentifier(CompetitionId("snarf"))

        assertThat(actual).isNull()
    }

    @Test
    fun `saving a Competition with unpersisted Challenges`() {
        val snowCase2018 = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        testEntityManager.persist(snowCase2018)

        val existingCompetition = competitionRepository.findByCompetitionIdentifier(CompetitionId("SnowCase2018"))!!

        val someChallenge = Challenge.defaultChallengeForTest()
        existingCompetition.addChallenge(someChallenge)

        competitionRepository.save(existingCompetition)
        testEntityManager.flush()

        val updatedCompetition = competitionRepository.findByCompetitionIdentifier(CompetitionId("SnowCase2018"))!!
        assertThat(updatedCompetition.challenges).containsExactly(someChallenge)
    }

    @Test
    fun `saving a Competition with unpersisted Competitors`() {
        val snowCase2018 = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        testEntityManager.persist(snowCase2018)
        testEntityManager.flush()

        snowCase2018.addCompetitor(CompetitorName("snarf"))

        competitionRepository.save(snowCase2018)
        testEntityManager.flush()
        testEntityManager.clear()

        val fetchedSnowcase = testEntityManager.find(Competition::class.java, snowCase2018.id)
        assertThat(fetchedSnowcase.competitors)
                .extracting<String> { it.name }
                .containsExactly("snarf")
    }

    @Test
    fun `removing Competitor, also cleans up the table`() {
        val snarf = CompetitorName(name = "snarf")
        val lionO = CompetitorName(name = "Lion-O")

        val competition = Competition.defaultCompetitionForTest(competitors = listOf(snarf, lionO))
        competitionRepository.save(competition)
        testEntityManager.flush()
        testEntityManager.clear()

        val retrievedCompetition = competitionRepository.getOne(competition.id)
        val lionOId = retrievedCompetition.findCompetitor("Lion-O").id
        retrievedCompetition.removeCompetitor(lionOId)
        competitionRepository.save(retrievedCompetition)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(competitorRepository.findByIdOrNull(lionOId)).isNull()
    }

    @Test
    fun `removing Challenge, also cleans up the table`() {
        val snarf = Challenge.defaultChallengeForTest(name = "snarf")
        val lionO = Challenge.defaultChallengeForTest(name = "Lion-O")
        testEntityManager.persist(snarf)
        testEntityManager.persist(lionO)
        testEntityManager.flush()
        testEntityManager.clear()

        val competition = Competition.defaultCompetitionForTest(challenges = listOf(snarf, lionO))
        competitionRepository.save(competition)
        testEntityManager.flush()
        testEntityManager.clear()

        val retrievedCompetition = competitionRepository.getOne(competition.id)
        retrievedCompetition.removeChallenge(lionO.id)
        competitionRepository.save(retrievedCompetition)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(challengeRepository.findByIdOrNull(lionO.id)).isNull()
    }

    @Test
    fun `deleting a Competition, also deletes its Competitors`() {
        val snarfName = CompetitorName(name = "snarf")
        val lionOName = CompetitorName(name = "Lion-O")

        val competition = Competition.defaultCompetitionForTest(competitors = listOf(snarfName, lionOName))
        competitionRepository.save(competition)
        testEntityManager.flush()
        testEntityManager.clear()

        val retrievedCompetition = competitionRepository.getOne(competition.id)
        val snarf = retrievedCompetition.findCompetitor("snarf")
        val lionO = retrievedCompetition.findCompetitor("Lion-O")
        competitionRepository.delete(retrievedCompetition)
        testEntityManager.flush()
        testEntityManager.clear()

        assertThat(competitorRepository.findByIdOrNull(snarf.id)).isNull()
        assertThat(competitorRepository.findByIdOrNull(lionO.id)).isNull()
        assertThat(competitionRepository.findByIdOrNull(competition.id)).isNull()
    }

    @Test
    fun `Uniqueness on CompetitionId`() {
        val snowCase2018 = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        testEntityManager.persist(snowCase2018)

        val competitionWithSameCompetitionId = Competition.defaultCompetitionForTest(name = "SnowCase2018")
        assertThatThrownBy {
            competitionRepository.save(competitionWithSameCompetitionId)
            testEntityManager.flush()
        }.hasCauseInstanceOf(ConstraintViolationException::class.java)
    }
}
