package com.motorro.statemachine.skills.app

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowViewModel
import com.motorro.statemachine.skills.app.data.MainGesture
import com.motorro.statemachine.skills.app.data.MainUiState
import com.motorro.statemachine.skills.app.state.MainStateFactory
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@KoinViewModel
internal class MainViewModel(api: MainApi) : CommonFlowViewModel<MainGesture, MainUiState, Unit, Unit>(
    api = api,
    init = Unit
)

@Factory
internal class MainApi : CommonFlowDataApi<MainGesture, MainUiState, Unit, Unit>, KoinComponent {
    override fun init(flowHost: CommonFlowHost<Unit>, input: Unit): CommonMachineState<MainGesture, MainUiState> {
        val factory = this@MainApi.get<MainStateFactory> {
            parametersOf(flowHost)
        }
        return factory.auth()
    }
    override fun getDefaultUiState(): MainUiState = MainUiState.Splash
    override fun getBackGesture(): MainGesture = MainGesture.Back
}