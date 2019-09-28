package be.swsb.effit.domain.core.exceptions

abstract class DomainRuntimeException(override val message: String?,
                                      private val status: HttpStatusCode): RuntimeException(message) {
    val httpStatusCode: HttpStatusCode
        get() {
            return status
        }
}

open class DomainValidationRuntimeException(message: String?): DomainRuntimeException(message, HttpStatusCode.`400`)
open class EntityNotFoundDomainRuntimeException(message: String?): DomainRuntimeException(message, HttpStatusCode.`404`)

sealed class HttpStatusCode {
    object `400`: HttpStatusCode()
    object `404`: HttpStatusCode()
}
