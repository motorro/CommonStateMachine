package com.motorro.commonstatemachine.skills.domain.friends

import com.motorro.commonstatemachine.skills.domain.friends.data.FriendData

/**
 * Creates a new friend
 */
interface CreateFriend {
    suspend operator fun invoke(data: FriendData)
}