package be.swsb.effit.competition

import java.time.LocalDate

class Competition private constructor(val name: String? = "MyCompetition",
                                      val startDate: LocalDate,
                                      val endDate: LocalDate) {

    companion object {
        fun competitionWithoutStartDate(name: String? = null, endDate: LocalDate): Competition {
            return Competition(name, startDate = LocalDate.now(), endDate = endDate)
        }
        fun competitionWithoutEndDate(name: String? = null, startDate: LocalDate): Competition {
            return Competition(name = name, startDate = startDate, endDate = startDate.plusDays(10))
        }
    }

}