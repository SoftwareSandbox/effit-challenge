package be.swsb.effit

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/challenge")
class ChallengeResource {

    @GetMapping
    fun allChallenges() : ResponseEntity<Challenge> {
        return ResponseEntity.ok(Challenge("RickRolled", 7, "ride down a slope with exposed torso"))
    }
}