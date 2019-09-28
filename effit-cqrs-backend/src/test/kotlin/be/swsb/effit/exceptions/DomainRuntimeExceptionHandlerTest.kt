package be.swsb.effit.exceptions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest

class DomainRuntimeExceptionHandlerTest {

    private val handler = DomainRuntimeExceptionHandler()

    @Test
    fun `handleDomainException should transform DomainValidationRuntime into an EffitError`() {
        val domainException = DomainValidationRuntimeException("test")

        val webRequest = Mockito.mock(WebRequest::class.java)

        val errorResponseEntity = handler.handleDomainException(domainException, webRequest)

        val errorBody : EffitError = errorResponseEntity.body as EffitError

        assertThat(errorBody.message).isEqualTo("test")
        assertThat(errorResponseEntity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `handleDomainException should transform EntityNotFoundDomainRuntimeException into an EffitError`() {
        val domainException = EntityNotFoundDomainRuntimeException("test")

        val webRequest = Mockito.mock(WebRequest::class.java)

        val errorResponseEntity = handler.handleDomainException(domainException, webRequest)

        val errorBody : EffitError = errorResponseEntity.body as EffitError

        assertThat(errorBody.message).isEqualTo("test")
        assertThat(errorResponseEntity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}