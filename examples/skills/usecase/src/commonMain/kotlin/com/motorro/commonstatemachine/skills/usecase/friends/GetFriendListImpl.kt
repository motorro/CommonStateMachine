package com.motorro.commonstatemachine.skills.usecase.friends

import com.motorro.commonstatemachine.skills.domain.friends.GetFriendList
import com.motorro.commonstatemachine.skills.domain.friends.data.Friend
import com.motorro.commonstatemachine.skills.usecase.Fixtures
import com.motorro.commonstatemachine.skills.usecase.Fixtures.NETWORK_DELAY
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
internal class GetFriendListImpl : GetFriendList {
    override fun invoke(): Flow<List<Friend>> = flow {
        Napier.i { "Loading friend list..." }
        delay(NETWORK_DELAY)
        emit(Fixtures.FRIENDS)
    }
}