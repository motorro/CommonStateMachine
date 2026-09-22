package com.motorro.statemachine.skills.app.state

import com.motorro.commonstatemachine.skills.domain.session.SessionManager
import com.motorro.commonstatemachine.skills.domain.session.data.Session
import com.motorro.statemachine.skills.app.data.MainGesture
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

internal class CheckingAuthentication(
    context: MainContext,
    private val sessionManager: SessionManager
) : BaseMainState(context) {
    override fun doStart() {
        setUiState(renderer.renderSplash())
        checkAuthentication()
    }

    private fun checkAuthentication() = stateScope.launch {
        Napier.d { "Checking current session..." }
        when(sessionManager.session.catch { emit(Session.None) }.first()) {
            is Session.Active -> {
                Napier.d { "Authenticated. Transferring to content..." }
                setMachineState(factory.content())
            }
            Session.None -> {
                Napier.d { "No active user. Requiring authentication..." }
                setMachineState(factory.auth())
            }
        }
    }

    override fun doProcess(gesture: MainGesture) {
        when(gesture) {
            is MainGesture.Back -> {
                Napier.d { "Back requested. Terminating..." }
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }

    @Factory
    class StateFactory(private val sessionManager: SessionManager) {
        fun create(context: MainContext) = CheckingAuthentication(
            context = context,
            sessionManager = sessionManager
        )
    }
}