package be.swsb.effit.backend.domain.command.competition

import be.swsb.effit.backend.domain.RestApiExposed

data class ChallengeToAdd(val name: String,
                          val points: Int,
                          val description: String) : RestApiExposed
