package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.util.RestApiExposed
import java.time.LocalDate
import java.util.*
import javax.persistence.*

//TODO Competitor should also have total points?
typealias Competitor = CompetitorName

@Entity
class Competition private constructor(@Id val id: UUID = UUID.randomUUID(),
                                      val name: String,
                                      val startDate: LocalDate,
                                      val endDate: LocalDate) : RestApiExposed {

    @OneToMany
    @JoinColumn(name = "FK_COMPETITION_ID")
    private var _challenges: List<Challenge> = emptyList()


    private var _competitors: List<Competitor> = emptyList()

    @Embedded
    private var competitionIdentifier: CompetitionId

    val challenges: List<Challenge>
        get() = _challenges

    val competitors: List<Competitor>
        get() = _competitors

    val competitionId: CompetitionId
        get() = competitionIdentifier

    fun addChallenge(challenge: Challenge) {
        _challenges = _challenges + challenge
    }

    fun addCompetitor(competitor: Competitor) {
        _competitors = _competitors + competitor
    }

    init {
        if (endDate.isBefore(startDate)) throw IllegalArgumentException("The end date can not be before the start date")
        competitionIdentifier = CompetitionId(name)
    }

    companion object {
        fun competition(name: String = "MyCompetition", startDate: LocalDate, endDate: LocalDate): Competition {
            return Competition(name = name, startDate = startDate, endDate = endDate)
        }

        fun competitionWithoutStartDate(name: String = "MyCompetition", endDate: LocalDate): Competition {
            return Competition(name = name, startDate = LocalDate.now(), endDate = endDate)
        }

        fun competitionWithoutEndDate(name: String = "MyCompetition", startDate: LocalDate): Competition {
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
}
