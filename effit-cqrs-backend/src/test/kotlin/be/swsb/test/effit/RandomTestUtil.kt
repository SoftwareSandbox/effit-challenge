package be.swsb.test.effit

import org.assertj.core.internal.bytebuddy.utility.RandomString

class RandomTestUtil {
    companion object {
        fun randomString(length: Int): String {
            return RandomString.make(length)
        }
    }
}
