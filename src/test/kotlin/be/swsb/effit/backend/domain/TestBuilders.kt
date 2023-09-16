package be.swsb.effit.backend.domain

import be.swsb.effit.backend.domain.command.competition.ChallengeToAdd
import be.swsb.effit.backend.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.backend.domain.core.challenge.Challenge
import be.swsb.effit.backend.domain.core.competition.Competition
import be.swsb.effit.backend.domain.core.competition.competitor.Competitor
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

fun aDefaultCompetitorForTest(
    id: UUID = UUID.randomUUID(),
    name: String = RandomString.make(),
    completedChallenges: List<Challenge> = emptyList()
): Competitor {
    val competitor = Competitor(id = id, name = name)
    completedChallenges.forEach { competitor.completeChallenge(it) }
    return competitor
}

fun aDefaultChallengeForTest(
    id: UUID = UUID.randomUUID(),
    name: String = RandomString.make(),
    points: Int = 3,
    description: String = RandomString.make()
) = Challenge(
    id = id,
    name = name,
    points = points,
    description = description
)

fun aChallengeFromChallengeToAdd(challengeToAdd: ChallengeToAdd) =
    Challenge(
        name = challengeToAdd.name,
        points = challengeToAdd.points,
        description = challengeToAdd.description
    )

fun aDefaultChallengeToAddForTest(
    name: String = RandomString.make(),
    points: Int = 3,
    description: String = RandomString.make()
) = ChallengeToAdd(name = name, points = points, description = description)


fun aDefaultCompetitionForTest(
    name: String = "Default Competition",
    startDate: LocalDate = LocalDate.of(2019, 4, 1),
    endDate: LocalDate = LocalDate.of(2019, 4, 11),
    competitors: List<CompetitorName> = emptyList(),
    challenges: List<ChallengeToAdd> = emptyList(),
    started: Boolean = false
) = Competition.competition(name = name, startDate = startDate, endDate = endDate).apply {
    competitors.forEach { addCompetitor(it) }
    challenges.forEach { addChallenge(it) }
    if (started) {
        start()
    }
}

fun aDefaultStartedCompetition(
    name: String = "Default Started Competition",
    startDate: LocalDate = LocalDate.of(2019, 4, 1),
    endDate: LocalDate = LocalDate.of(2019, 4, 11),
    competitors: List<CompetitorName> = listOf(randomCompetitorName()),
    challenges: List<ChallengeToAdd> = listOf(aDefaultChallengeToAddForTest())
) = aDefaultCompetitionForTest(name, startDate, endDate, competitors, challenges, true)

fun aDefaultCompetitionWithChallengesAndCompetitorsForTest(
    name: String = "Default Competition",
    startDate: LocalDate = LocalDate.of(2019, 4, 1),
    endDate: LocalDate = LocalDate.of(2019, 4, 11),
    competitors: List<CompetitorName> = listOf(randomCompetitorName()),
    challenges: List<ChallengeToAdd> = listOf(aDefaultChallengeToAddForTest()),
    started: Boolean = false
) = aDefaultCompetitionForTest(name, startDate, endDate, competitors, challenges, started)

fun Competition.findCompetitor(name: String): Competitor {
    return this.competitors.find { it.name == name }
        ?: throw IllegalStateException("Expected competition to have Competitor with name $name")
}

fun Competition.findChallenge(name: String): Challenge {
    return this.challenges.find { it.name == name }
        ?: throw IllegalStateException("Expected competition to have Challenge with name $name")
}

fun randomCompetitorName() = CompetitorName(RandomTestUtil.randomString(50))


object RandomString {
    private val alphabet = 'a'..'z'
    private val alphabetUppercase = 'A'..'Z'
    private val numericals = '0'..'9'
    private val specialChars = listOf(',', '.', '/', '`', '~', '\'', '"', '\\', '[', ']', '{', '}', '(', ')', '-', '_', '=', '+', '!', '@', '#', '$', '%', '^', '&', '*', '|', ';', ':')
    private val chars = alphabet + alphabetUppercase + numericals + specialChars
    private val alphaNumericalChars = chars - specialChars.toSet()

    fun make(length: Int = 20, alphaNumeric: Boolean = true) = (1..length).joinToString(separator = "") {
        val charSet = if(alphaNumeric) alphaNumericalChars else chars
        "${charSet[Random.nextInt(0, charSet.size-1)]}"
    }
}

class RandomTestUtil {
    companion object {
        fun randomString(length: Int) = RandomString.make(length)
    }
}
