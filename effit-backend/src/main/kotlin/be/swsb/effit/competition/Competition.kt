package be.swsb.effit.competition

import be.swsb.effit.challenge.Challenge
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
class Competition private constructor(@Id val id: UUID = UUID.randomUUID(),
                                      val name: String,
                                      val startDate: LocalDate,
                                      val endDate: LocalDate) {

    @OneToMany
    @JoinColumn(name = "FK_COMPETITION_ID")
    private var _challenges: List<Challenge> = emptyList()

    @Embedded
    private var competitionIdentifier: CompetitionId

    val challenges: List<Challenge>
        get() = _challenges

    val competitionId: CompetitionId
        get() = competitionIdentifier

    fun addChallenge(challenge: Challenge) {
        _challenges = _challenges + challenge
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
data class CompetitionId constructor(@Column(name = "COMPETITION_ID") private val internalId: String) {

    val id: String
        get() {
            return `remove characters considered ugly in a URL`()
        }

    private fun `remove characters considered ugly in a URL`(): String {
        val spaceRemoved = listOf(" ")
        val reservedCharacters = listOf(";", "/", "?", ":", "@", "=", "&")
        val unsafeCharacters = listOf("\"", "<", ">", "#", "%", "{", "}", "|", "\\", "^", "~", "[", "]", "`")
        return (spaceRemoved + reservedCharacters + unsafeCharacters)
                .fold(internalId) { acc, reservedChar -> acc.replace(reservedChar, "") }
    }
}
