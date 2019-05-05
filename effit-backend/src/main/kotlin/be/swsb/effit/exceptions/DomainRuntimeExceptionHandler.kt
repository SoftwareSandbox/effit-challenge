package be.swsb.effit.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class DomainRuntimeExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [DomainRuntimeException::class])
    fun handleDomainException(ex: DomainRuntimeException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = EffitError(ex.message)
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), statusFrom(ex.httpStatusCode), request)
    }

    private fun statusFrom(httpStatusCode: HttpStatusCode): HttpStatus {
        return when(httpStatusCode) {
            HttpStatusCode.`403` -> HttpStatus.BAD_REQUEST
            HttpStatusCode.`404` -> HttpStatus.NOT_FOUND
        }
    }
}