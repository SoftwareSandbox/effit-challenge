package be.swsb.effit.challenge

import be.swsb.effit.exceptions.EntityNotFoundDomainRuntimeException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api/challenge",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class ChallengeController(private val challengeRepository: ChallengeRepository) {

    @GetMapping
    fun allChallenges(): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok(challengeRepository.findAll())
    }

    @GetMapping("{challengeId}")
    fun challengeDetail(@PathVariable(value = "challengeId") challengeId: String): ResponseEntity<Challenge> {
        return challengeRepository.findByIdOrNull(UUID.fromString(challengeId))
                ?.let { ResponseEntity.ok(it) }
                ?: throw EntityNotFoundDomainRuntimeException("Challenge with id $challengeId not found")
    }

    @PostMapping
    fun createChallenge(@RequestBody createChallenge: CreateChallenge): ResponseEntity<Any> {
        val createdChallengeId: String = challengeRepository.save(createChallenge).id.toString()
        return ResponseEntity.created(URI(createdChallengeId)).build()
    }

    @PutMapping("{challengeId}")
    fun updateChallenge(@PathVariable(value = "challengeId") challengeId: String,
                        @RequestBody updatedChallenge: Challenge): ResponseEntity<Any> {
        return challengeRepository.findByIdOrNull(UUID.fromString(challengeId))
                ?. let {
                    challengeRepository.save(
                            it.copy(name = updatedChallenge.name,
                            points = updatedChallenge.points,
                            description = updatedChallenge.description)
                    )
                    ResponseEntity.ok().build<Any>()
                }
                ?: throw EntityNotFoundDomainRuntimeException("Challenge with id $challengeId not found")
    }
}