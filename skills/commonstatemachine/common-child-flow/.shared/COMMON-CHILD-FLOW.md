# Common Child Flow

![Common Child Flow Diagram](common-child-flow.puml)

## Philosophy and Principles
- **Modularity via Proxying:** Features are independent modules. A parent flow hosts a child flow using `ProxyMachineState`, which bridges incompatible Gesture/UI systems.
- **Direct Data Flow:** Transitions occur in memory. Complex data or even running coroutines can be passed between states without serialization or "parceling".
- **KMP Ready:** Logic and Data are isolated from the View, making the core feature logic easily shareable across platforms.
- **Standardized Child Flows:** 
    - `commonflow-data`: Standardizes flow initialization and result handling.
    - `commonflow-compose`: Provides a unified way to inject UI into a composition.
    - `commonflow-viewmodel`: Bridges the state machine to the platform's Lifecycle and Navigation frameworks.

## Data API
Data API is defined in the `commonflow-data` module and consists of two primary interfaces:

### `CommonFlowDataApi<G, U, I, R>`
This is the **Feature Entry Point**. It is defined in the feature module and provides the host with everything needed to launch the child flow.
- **`init(flowHost: CommonFlowHost<R>, input: I)`**: Initializes the child flow and returns the first `MachineState`. It injects the `flowHost` so 
  the child can signal when it's done.
- **`getDefaultUiState()`**: Provides the initial state (like "Loading") to be displayed while the child machine is starting up.
- **`getBackGesture()`**: Defines which gesture represents "Back" in the feature, allowing the host to bridge system back-presses to the child logic.

### `CommonFlowHost<in R>`
This is the **Communication Bridge**. It is implemented by the parent's `ProxyMachineState` and passed to the child flow.
- **`onComplete(result: R)`**: The child flow calls this method when it reaches a terminal state. The host implementation then receives the `result` and handles 
  the transition to the next part of the parent flow.

## UI API
UI API is defined in the `commonflow-compose` module and consists of the following interface:

### `CommonFlowUiApi<G, U>`
This interface provides the **Feature View**. It is implemented in the feature module using `Compose` and is used by the host to render the child flow's UI.

- **`Screen(state: U, onGesture: (G) -> Unit, modifier: Modifier)`**: A `@Composable` function that takes the current feature **UI State (U)** 
  and a gesture handler for feature **Gestures (G)**. It encapsulates all the UI logic for the feature, allowing the host to simply plug it into its composition.

This separation ensures that the host doesn't need to know about the internal UI components of the child flow, only how to provide the state and handle 
the gestures, which are bridged by the `ProxyMachineState`.
