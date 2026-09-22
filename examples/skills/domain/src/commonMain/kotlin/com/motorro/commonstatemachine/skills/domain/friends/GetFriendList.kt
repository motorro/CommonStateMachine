package com.motorro.commonstatemachine.skills.domain.friends

import com.motorro.commonstatemachine.skills.domain.friends.data.Friend

/**
 * Retrieves a friend list
 */
interface GetFriendList {
    suspend operator fun invoke(): List<Friend>
}