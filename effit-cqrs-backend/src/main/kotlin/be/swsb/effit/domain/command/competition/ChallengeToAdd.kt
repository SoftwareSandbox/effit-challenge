package be.swsb.effit.domain.command.competition

import be.swsb.effit.adapter.ui.util.RestApiExposed

data class ChallengeToAdd(val name: String,
                          val points: Int,
                          val description: String) : RestApiExposed {
    companion object
}
