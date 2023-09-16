package be.swsb.effit.backend.domain.core.users

import be.swsb.effit.backend.domain.core.exceptions.DomainValidationRuntimeException
import java.util.UUID

class User(private val userId: UUID, private val name: UserName, private val role: Role)

@JvmInline
value class UserName(val value: String) {
    init {
        if (value.isEmpty()) {
            throw DomainValidationRuntimeException("Username cannot be empty.")
        }
    }
}