package be.swsb.effit.competition

import be.swsb.effit.util.RestApiExposed
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

//TODO Competitor should also have total points?
@Entity
data class Competitor(@Id val id: UUID = UUID.randomUUID(),
                      val name: String): RestApiExposed