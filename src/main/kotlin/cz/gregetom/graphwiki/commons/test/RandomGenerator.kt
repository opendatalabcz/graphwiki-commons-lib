package cz.gregetom.graphwiki.commons.test

import org.apache.commons.lang3.RandomStringUtils
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import kotlin.random.Random

object RandomGenerator {

    val instance = EasyRandom(
            EasyRandomParameters()
                    .seed(Random.nextLong())
                    .stringLengthRange(1, 25)
                    .collectionSizeRange(0, 10)
    )

    fun randomString(length: Int): String {
        require(length > 0) { "Length must be greater than 0!" }
        return RandomStringUtils.randomAlphanumeric(length)
                ?: throw IllegalStateException("Should not happened!")
    }
}
