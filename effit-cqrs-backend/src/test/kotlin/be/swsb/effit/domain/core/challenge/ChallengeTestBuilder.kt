package be.swsb.effit.domain.core.challenge

import org.assertj.core.internal.bytebuddy.utility.RandomString
import java.util.*

fun Challenge.Companion.defaultChallengeForTest(
        id: UUID = UUID.randomUUID(),
        name: String = RandomString.make(),
        points: Int = 3,
        description: String = RandomString.make()) : Challenge {
    return Challenge(id = id, name = name, points = points, description = description)
}
