package com.motorro.commonstatemachine.skills.domain.friends

import com.motorro.commonstatemachine.skills.domain.friends.data.Friend
import kotlinx.coroutines.flow.Flow

/**
 * Retrieves a friend list
 */
interface GetFriendList {
    operator fun invoke(): Flow<List<Friend>>
}