package be.swsb.effit.domain.command.competition

import org.assertj.core.internal.bytebuddy.utility.RandomString

fun ChallengeToAdd.Companion.defaultChallengeToAddForTest(
        name: String = RandomString.make(),
        points: Int = 3,
        description: String = RandomString.make()) : ChallengeToAdd {
    return ChallengeToAdd(name = name, points = points, description = description)
}
