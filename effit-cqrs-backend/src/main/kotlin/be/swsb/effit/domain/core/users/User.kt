package be.swsb.effit.domain.core.users

import be.swsb.effit.domain.core.exceptions.DomainValidationRuntimeException
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