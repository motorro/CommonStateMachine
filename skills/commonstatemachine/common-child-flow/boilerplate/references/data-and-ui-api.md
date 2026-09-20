# Module API: data and UI API
Use this reference to create the required data and UI APIs.

## Mechanics
The [Common Child Flow](https://github.com/motorro/CommonStateMachine/tree/master#common-child-flow-api) consists of two main interfaces:

- A subtype of [CommonFlowDataApi](https://github.com/motorro/CommonStateMachine/blob/master/commonflow/data/src/commonMain/kotlin/com/motorro/commonstatemachine/flow/data/CommonFlowDataApi.kt).
  This interface binds together the gesture-system, ui-system, input and output providing the child flow initialization.
  The API implementation is then created in the implementation module and could be injected into the parent flow.
- A subtype of [CommonFlowUiApi](https://github.com/motorro/CommonStateMachine/blob/master/commonflow/compose/src/commonMain/kotlin/com/motorro/commonstatemachine/flow/compose/CommonFlowUiApi.kt).
  This interface binds gesture-system and ui-system with the composable responsible for the view rendering.
  The API implementation is then created in the implementation module and could be injected into the composition using locals or any other way.

## Step 1. Data flow
Take the created [Gestures and UI-states](gesture-and-ui-state-api.md), [input and result](input-and-result-api.md) and create an interface or a typealias
to bind the generics of the `com.motorro.commonstatemachine.flow.data.CommonFlowDataApi`. This API would be available to the flow consumer to start the feature flow.
Take a look at the [class diagram](common-child-flow.puml) for more details.
Take a look at the [example](../assets/example/api/AuthDataApi.kt) - data API definition

## Step 2. UI flow
Take the created [Gestures and UI-states](gesture-and-ui-state-api.md) and create an interface or a typealias
to bind the generics of the `com.motorro.commonstatemachine.flow.compose.CommonFlowUiApi`. This API would be available to the flow consumer to display the feature.
Take a look at the [class diagram](common-child-flow.puml) for more details.
Take a look at the [example](../assets/example/api/AuthUiApi.kt) - UI API definition
