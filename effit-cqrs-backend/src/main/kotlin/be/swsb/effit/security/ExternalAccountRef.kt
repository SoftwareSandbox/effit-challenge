package be.swsb.effit.security

typealias JwtIss = String
typealias JwtSub = String

data class ExternalAccountRef(
    val jwtIss: JwtIss, val jwtSub: JwtSub
)