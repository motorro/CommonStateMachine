# Module implementation: flow proxy
The common-child-flow module is intended to be used inside a hosting proxy state.
If the user asks you to integrate the feature-flow into a parent/hosting flow (e.g., an application main flow or onboarding flow), you must create a `ProxyMachineState` in the parent module.

For a deeper dive into the state machine mechanics and common components, refer to the [Core Architecture](ARCHITECTURE.md).
For a deeper dive into the common child flow philosophy and design, refer to the [Common Child Flow Architecture](COMMON-CHILD-FLOW.md).

## Step 1. Preparing the host flow (Gesture & UI-State Adapters)
To run a child feature flow inside a parent state machine, the parent's Gesture and UI-state systems must be extended to wrap the child's gestures and UI states.

- Proxy Gesture: In the parent's sealed Gesture class/structure, add a wrapper case containing the child's feature gesture.
  Example: `data class Auth(val child: AuthGesture) : MainGesture()`.
  See the [example gesture](../assets/example/app/data/MainGesture.kt).
- Proxy UI-State: In the parent's sealed UI-state class/structure, add a wrapper case containing the child's feature UI-state.
  Example: `data class Auth(val child: AuthUiState) : MainUiState()`.
  See the [example UI state](../assets/example/app/data/MainUiState.kt).

## Step 2. Implement parent proxy state
Create a `ProxyMachineState` subclass in the parent module that encapsulates the child state machine flow.

The proxy state must:

- Extend `ProxyMachineState<ParentGesture, ParentUiState, ChildGesture, ChildUiState>`. Pass `api.getDefaultUiState()` as the initial UI state to the super constructor.
- Implement `CommonFlowHost<ChildResult>`. Create a `flowHost` instance that handles the child flow completion. When `onComplete(result)` is called
  by the child, update the parent machine state using `setMachineState(...)` to transition to the next parent state based on the result.
- Override `init()`. Call `api.init(flowHost, input)` to create and return the child state machine's starting state.
- Override `mapGesture(parent: ParentGesture)`. Unwraps the child gesture from the parent proxy gesture wrapper (e.g., `MainGesture.Auth -> parent.child`).
  Additionally, map system-level parent gestures (such as `MainGesture.Back`) to the child's back gesture (`api.getBackGesture()`) if appropriate.
- Override `mapUiState(child: ChildUiState)`. Wraps the child's UI state into the parent's UI state wrapper (e.g., `MainUiState.Auth(child)`).

See the [example proxy state](../assets/example/app/state/AuthProxyState.kt) for a complete implementation.

## Step 3. Create a parent state-factory method & DI setup
To instantiate the proxy state cleanly within the parent flow:

- Local Proxy Factory: Create an inner factory class or separate factory class for the proxy state, named `StateFactory` (e.g., `@Factory class StateFactory(private val api: AuthDataApi)`).
  This factory should inject the child feature's `DataApi` (e.g., `AuthDataApi`) and instantiate `AuthProxyState(context, ..., api)`.
- Parent State Factory Interface: Add a method in the parent's `StateFactory` interface to produce the proxy state (e.g., `fun auth(): MainState`).
- Parent State Factory Implementation: Inject the proxy state factory into the parent `StateFactory` implementation, and delegate
  the state creation call to it.
- DI Registration: Ensure the proxy state factory and parent state factory implementation are annotated
  for DI (e.g., `@Factory` for Koin, `@Inject` / `@ViewModelScoped` for Hilt).

See the [example main state factory](../assets/example/app/state/MainStateFactory.kt) and [proxy state factory](../assets/example/app/state/AuthProxyState.kt).
