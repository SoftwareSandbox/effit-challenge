package be.swsb.effit.adapter.ui.challenge

import be.swsb.effit.adapter.sql.challenge.ChallengeRepository
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.core.exceptions.EntityNotFoundDomainRuntimeException
import be.swsb.effit.domain.query.QueryExecutor
import be.swsb.effit.domain.query.challenge.FindChallenge
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/challenge",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class ChallengeController(private val challengeRepository: ChallengeRepository,
                          private val queryExecutor: QueryExecutor) {

    @GetMapping("{challengeId}")
    fun challengeDetail(@PathVariable(value = "challengeId") challengeId: String): ResponseEntity<Challenge> {
        return queryExecutor.execute<Challenge>(FindChallenge(UUID.fromString(challengeId)))
                ?.let { ResponseEntity.ok(it) }
                ?: throw EntityNotFoundDomainRuntimeException("Challenge with id $challengeId not found")
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
