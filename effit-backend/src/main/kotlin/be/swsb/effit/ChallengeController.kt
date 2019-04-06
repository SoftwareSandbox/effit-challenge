package be.swsb.effit

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/challenge",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class ChallengeController {

    @GetMapping
    fun allChallenges() : ResponseEntity<List<Challenge>> {
        val someChallenge = Challenge("Playboy", 7, "ride down a slope with exposed torso")
        return ResponseEntity.ok(listOf(someChallenge))
    }
}