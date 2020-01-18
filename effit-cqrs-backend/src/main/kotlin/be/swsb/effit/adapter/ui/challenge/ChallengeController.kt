package be.swsb.effit.adapter.ui.challenge

import be.swsb.effit.domain.command.challenge.UpdateChallenge
import be.swsb.effit.domain.core.challenge.Challenge
import be.swsb.effit.domain.query.challenge.FindChallenge
import be.swsb.effit.messaging.command.CommandExecutor
import be.swsb.effit.messaging.query.QueryExecutor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/challenge",
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class ChallengeController(private val commandExecutor: CommandExecutor,
                          private val queryExecutor: QueryExecutor) {

    @GetMapping("{challengeId}")
    fun challengeDetail(@PathVariable(value = "challengeId") challengeId: String): ResponseEntity<Challenge> {
        return queryExecutor.execute(FindChallenge(UUID.fromString(challengeId)))
                .let { ResponseEntity.ok(it) }
    }

    @PutMapping("{challengeId}")
    fun updateChallenge(@PathVariable(value = "challengeId") challengeId: String,
                        @RequestBody updateChallenge: UpdateChallenge): ResponseEntity<Any> {
        commandExecutor.execute(updateChallenge)
        return ResponseEntity.ok().build<Any>()
    }
}
