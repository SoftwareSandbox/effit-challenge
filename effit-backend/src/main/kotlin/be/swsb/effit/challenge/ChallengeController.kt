package be.swsb.effit.challenge

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.net.URI

@RestController
@RequestMapping("/api/challenge",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class ChallengeController(private val challengeRepository: ChallengeRepository) {

    @GetMapping
    fun allChallenges() : ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok(challengeRepository.findAll())
    }

    @PostMapping
    fun createChallenge(@RequestBody createChallenge: CreateChallenge): ResponseEntity<Any> {
        try {
            val createdChallengeId : String = challengeRepository.save(createChallenge).id.toString()
            return ResponseEntity.created(URI(createdChallengeId)).build()
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went horribly wrong", e)
        }
    }
}