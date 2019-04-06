package be.swsb.effit

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.specs.StringSpec

class ChallengeTest : DescribeSpec({
    describe("Challenge construction") {
        it("should fail with negative points") {
            shouldThrowExactly<IllegalStateException> {
                Challenge(name = "Playboy", points = -7, description = "ride down a slope with exposed torso")
            }.message shouldBe "Cannot create a Challenge with negative points"
        }
        it("should fail with 0 points") {
            shouldThrowExactly<IllegalStateException> {
                Challenge(name = "Playboy", points = 0, description = "ride down a slope with exposed torso")
            }.message shouldBe "Cannot create a Challenge with 0 points"
        }
    }
})
