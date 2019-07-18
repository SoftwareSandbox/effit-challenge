package be.swsb.effit

import be.swsb.effit.challenge.Challenge
import be.swsb.effit.challenge.ChallengeRepository
import be.swsb.effit.competition.Competition
import be.swsb.effit.competition.CompetitionRepository
import be.swsb.effit.competition.competitor.CompetitorRepository
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalDate


@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
class EffitApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EffitApplication::class.java, *args)
        }
    }
}

@Profile("local")
@Component
class AppStartupRunner(private val challengeRepository: ChallengeRepository,
                       private val competitionRepository: CompetitionRepository,
                       private val competitorRepository: CompetitorRepository) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        competitorRepository.deleteAll()
        challengeRepository.deleteAll()
        happyLittleChallenges.map { challengeRepository.save(it) }
        val snowcase2018 = Competition.competition("Snowcase 2018", LocalDate.of(2018, 3, 16), LocalDate.of(2018, 3, 26))
        happyLittleChallenges.map { snowcase2018.addChallenge(it) }
        competitionRepository.deleteAll()
        competitionRepository.save(snowcase2018)
    }

    companion object {
        var happyLittleChallenges = listOf(
        Challenge(name = "Playboy", points = 7, description="Go down a slope with your torso exposed. Yell PHUC IT when you finished the slope, of course girls can keep their bra on."),
        Challenge(name = "Picasso", points = 3, description="Draw something on the head/face of another player, while remaining unnoticed."),
        Challenge(name = "Dali", points = 1, description=" As a victim of a Picasso, wear the drawing until the Ledger phase."),
        Challenge(name = "Early Bird", points = 3, description="Be the first on the skilift. Apart from the service personel you have to be the very first person on the lift. Pictures count as proof or witnesses."),
        Challenge(name = "Randy", points = 2, description=" Go down the slope and ride up to a group of strangers. Loudly claim to be the best skiier/snowboarder Val-Thorens has ever seen, finish with the hulk pose."),
        Challenge(name = "Usual Suspect", points = 3, description="Fart inside a gondola, and loudly apologize for farting."),
        Challenge(name = "Rock Star", points = 1, description="Perform a stage dive or crowd surf."),
        Challenge(name = "Allo Allo", points = 3, description="Speak with an accent (dialects are not accents) throughout the day until the Ledger phase (after leaving the ski room). Yell PHUC IT when the Ledger phase starts."),
        Challenge(name = "Groupie", points = 1, description="Take a selfie with a famous person."),
        Challenge(name = "Mannequin", points = 3, description="Wear 3 pieces of clothing from 3 different persons. Take the pieces unnoticed."),
        Challenge(name = "Human Fly", points = 2, description="Wear your ski goggles from 9:00 (or after exiting the ski room) until 17:00. Before taking the goggles off, say: \"Bzzz, bzzz, bzzz, I'm a human fly\"."),
        Challenge(name = "Bear Grylls", points = 1, description=" Finish a warm meal using only your knife."),
        Challenge(name = "Duff Man", points = 1, description=" Drink a beer within 7 seconds."),
        Challenge(name = "Stephen Hawking", points = 1, description=" Drink a shot without using your hands."),
        Challenge(name = "Gabriel", points = 2, description=" Make a snow angel and while lying face up yell: _God, I'm an angel, I've got my wings, come get me."),
        Challenge(name = "Home Sick", points = 1, description=" Ask a random stranger if you can do the dishes for them."),
        Challenge(name = "The Professional", points = 1, description=" Finish a black slope."),
        Challenge(name = "The Joker", points = 1, description=" Finish a house of cards, 4 stories high."),
        Challenge(name = "Sociaal Incapabele Michiel", points = 1, description=" During a lift ride, ask a stranger if they ever shit their pants while skiing."),
        Challenge(name = "Boulie Boulie", points = 1, description=" Make a snowman, height needs to be at least above your knees."),
        Challenge(name = "1-Trick Pony", points = 1, description=" To the best of your ability, perform a trick while riding down the slope.")
        )
    }
}