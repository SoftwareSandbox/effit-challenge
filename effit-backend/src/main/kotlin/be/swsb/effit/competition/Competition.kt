package be.swsb.effit.competition

import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Competition private constructor(@Id val id: UUID = UUID.randomUUID(),
                                      val name: String? = "MyCompetition",
                                      val startDate: LocalDate,
                                      val endDate: LocalDate) {

    init {
        if (endDate.isBefore(startDate)) throw IllegalArgumentException("The end date can not be before the start date")
    }

    companion object {
        fun competition(name: String? = null, startDate: LocalDate, endDate: LocalDate): Competition {
            return Competition(name = name, startDate = startDate, endDate = endDate)
        }

        fun competitionWithoutStartDate(name: String? = null, endDate: LocalDate): Competition {
            return Competition(name = name, startDate = LocalDate.now(), endDate = endDate)
        }

        fun competitionWithoutEndDate(name: String? = null, startDate: LocalDate): Competition {
            return Competition(name = name, startDate = startDate, endDate = startDate.plusDays(10))
        }
    }

}