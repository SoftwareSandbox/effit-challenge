package be.swsb.effit.backend.domain.core.users

enum class Role {
    Guest, //People that are just lurking or got invited to play in a competition
    Known, //People that took the time to create a user account, they can create a new competition
    Player, //The players in a competition, they can complete challenges
    CompetitionOwner, //The person that created the competition, there can be many per competition but initially there is one
}