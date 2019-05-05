package be.swsb.effit.exceptions

abstract class DomainRuntimeException(override val message: String?,
                                      private val status: HttpStatusCode): RuntimeException(message) {
    val httpStatusCode: HttpStatusCode
        get() {
            return status
        }
}

class DomainValidationRuntimeException(message: String?): DomainRuntimeException(message, HttpStatusCode.`403`)
class EntityNotFoundDomainRuntimeException(message: String?): DomainRuntimeException(message, HttpStatusCode.`404`)

sealed class HttpStatusCode {
    object `403`: HttpStatusCode()
    object `404`: HttpStatusCode()
}