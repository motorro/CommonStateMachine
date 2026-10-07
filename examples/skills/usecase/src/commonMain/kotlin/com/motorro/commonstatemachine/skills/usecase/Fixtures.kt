package com.motorro.commonstatemachine.skills.usecase

import com.motorro.commonstatemachine.skills.domain.authenticate.data.PasswordRequirements
import com.motorro.commonstatemachine.skills.domain.friends.data.Friend
import com.motorro.commonstatemachine.skills.domain.friends.data.FriendData
import com.motorro.commonstatemachine.skills.domain.session.data.Username
import kotlin.time.Duration.Companion.seconds

internal object Fixtures {

    val NETWORK_DELAY = 2.seconds

    object Authentication {
        val REQUIREMENTS = PasswordRequirements(
            regex = "^.{8,}$".toRegex(),
            description = "Minimum eight characters"
        )

        val USERNAME = Username("user")

        const val PASSWORD = "password"
    }

    val FRIENDS = listOf(
        Friend(1, FriendData("Alice", 28)),
        Friend(2, FriendData("Bob", 32)),
        Friend(3, FriendData("Charlie", 25)),
        Friend(4, FriendData("Diana", 30)),
        Friend(5, FriendData("Evan", 22))
    )
}

