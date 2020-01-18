package be.swsb.effit.domain.core.competition

import be.swsb.effit.adapter.ui.util.RestApiExposed
import be.swsb.effit.domain.command.competition.ChallengeToAdd
import be.swsb.effit.domain.command.competition.competitor.CompetitorName
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.competition.competitor.Competitor
import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
import com.fasterxml.jackson.annotation.JsonSetter
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
class Competition private constructor(@Id val id: UUID = UUID.randomUUID(),
                                      val name: String,
                                      val startDate: LocalDate,
                                      val endDate: LocalDate) : RestApiExposed {

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "FK_COMPETITION_ID")
    @JsonSetter("challenges")
    private var _challenges: MutableList<Challenge> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "FK_COMPETITION_ID")
    @JsonSetter("competitors")
    private var _competitors: MutableList<Competitor> = mutableListOf()

    @Embedded
    private var competitionIdentifier: CompetitionId

    val challenges: List<Challenge>
        get() = _challenges

    val competitors: List<Competitor>
        get() = _competitors

    val competitionId: CompetitionId
        get() = competitionIdentifier

    @Column(name = "started")
    private var _started: Boolean

    val started: Boolean
        get() = _started

    init {
        if (endDate.isBefore(startDate)) throw DomainValidationRuntimeException("The end date can not be before the start date")
        competitionIdentifier = CompetitionId(name)
        _started = false
    }

    fun addChallenge(challengeToAdd: ChallengeToAdd) {
        if (_started) {
            throw UnableToAddChallengeToStartedCompetitionDomainException()
        }
        val challenge = createChallenge(challengeToAdd)
        _challenges.add(challenge)
    }

    private fun createChallenge(challengeToAdd: ChallengeToAdd): Challenge {
        return Challenge(
                name = challengeToAdd.name,
                points = challengeToAdd.points,
                description = challengeToAdd.description
        )
    }

    fun removeChallenge(challengeId: UUID) {
        if (_started) {
            throw UnableToRemoveChallengeFromStartedCompetitionDomainException()
        }
        _challenges.find { it.id == challengeId }
                ?. let { challengeToBeRemoved -> _challenges.remove(challengeToBeRemoved) }
                ?: throw DomainValidationRuntimeException("No Challenge found on this competition for given id $challengeId")
    }

    fun addCompetitor(competitorName: CompetitorName) {
        if (_started) {
            throw UnableToAddCompetitorToStartedCompetitionDomainException()
        }
        val competitor = createCompetitor(competitorName)
        _competitors.add(competitor)
    }

    private fun createCompetitor(competitorName: CompetitorName): Competitor {
        return Competitor(name = competitorName.name)
    }

    fun removeCompetitor(competitorIdToBeRemoved: UUID) {
        if (_started) {
            throw UnableToRemoveCompetitorOfAStartedCompetitionDomainException()
        }
        _competitors.find { it.id == competitorIdToBeRemoved }
                ?. let { _competitors.remove(it) }
                ?: throw CompetitorNotFoundOnCompetitionDomainException()
    }

    fun start() {
        `don't start competition when no competitors`()
        `don't start competition when no challenges`()
        _started = true
    }

    private fun `don't start competition when no competitors`() {
        if (competitors.isEmpty()) {
            throw DomainValidationRuntimeException("Starting a competition without competitors is kind of useless, don't you think?")
        }
    }

    private fun `don't start competition when no challenges`() {
        if (challenges.isEmpty()) {
            throw DomainValidationRuntimeException("Starting a competition without challenges is kind of useless, don't you think?")
        }
    }

    fun unstart() {
        _started = false
    }

    fun completeChallenge(challenge: Challenge, competitorId: UUID) {
        if (this._started) {
            this.challenges.find { it == challenge }
                ?: throw DomainValidationRuntimeException("Can't complete a challenge that's not part of this competition.")
            this.competitors.find { it.id == competitorId }
                ?. completeChallenge(challenge)
                ?: throw DomainValidationRuntimeException("Can't complete a challenge for a non existing competitor.")
        } else {
            throw DomainValidationRuntimeException("Can't complete challenges on a competition that's not yet started.")
        }
    }

    companion object {
        fun competition(name: String, startDate: LocalDate, endDate: LocalDate): Competition {
            return Competition(name = name, startDate = startDate, endDate = endDate)
        }

        fun competitionWithoutStartDate(name: String, endDate: LocalDate): Competition {
            return Competition(name = name, startDate = LocalDate.now(), endDate = endDate)
        }

        fun competitionWithoutEndDate(name: String, startDate: LocalDate): Competition {
            return Competition(name = name, startDate = startDate, endDate = startDate.plusDays(10))
        }
    }

}

@Embeddable
class CompetitionId constructor(name: String) {

    @Column(name = "COMPETITION_ID")
    private var _id: String

    val id: String
        get() {
            return _id
        }

    init {
        _id = `remove characters considered ugly in a URL`(name)
        if (_id.isBlank()) {
            throw DomainValidationRuntimeException("Cannot create a CompetitionId from an empty name.")
        }
    }

    private fun `remove characters considered ugly in a URL`(someString: String): String {
        val spaceRemoved = listOf(" ")
        val reservedCharacters = listOf(";", "/", "?", ":", "@", "=", "&")
        val unsafeCharacters = listOf("\"", "<", ">", "#", "%", "{", "}", "|", "\\", "^", "~", "[", "]", "`")
        return (spaceRemoved + reservedCharacters + unsafeCharacters)
                .fold(someString) { acc, reservedChar -> acc.replace(reservedChar, "") }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompetitionId

        if (_id != other._id) return false

        return true
    }

    override fun hashCode(): Int {
        return _id.hashCode()
    }

    override fun toString(): String {
        return _id
    }
}
