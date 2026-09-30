# CommonStateMachine Architecture

![Architecture Diagram](architecture.puml)

## Core Mechanics
- **Machine State:** A self-contained logic unit. It handles **Gestures (G)** and produces **UI States (U)**.
- **Lifecycle:** 
    - `doStart()`: Called when state becomes active. Use for initialization or launching side effects.
    - `doProcess(gesture)`: Processes user input.
    - `doClear()`: Cleanup when leaving the state.
- **Transitions:** States call `setMachineState(nextState)` to move the machine forward. No external "router" is needed.
- **UI Updates:** States call `setUiState(newState)` to update the View.
- **State Machine:** The container for the current **Machine State**.
    - It maintains the state transition logic.
    - `FlowStateMachine` (standard implementation) exports the current **UI State** as a `StateFlow`.
    - It provides the `process(gesture)` entry point for user interaction.
- **Navigation Inversion:** Navigation is a logical state transition within the state machine (Logic/ViewModel layer). The View is passive and follows the logic.
- **ViewModel Integration:** The `ViewModel` (or equivalent lifecycle host) typically:
    - Holds the `StateMachine` instance.
    - Delegates user gestures to `stateMachine.process(gesture)`.
    - Collects and exposes `stateMachine.uiState`.
    - Calls `stateMachine.clear()` in its `onCleared()` lifecycle method to clean up resources.

## Extra Components-Helpers
- **State Factory:** Decouples state creation from the states themselves. This allows for easy dependency injection and makes unit testing transitions simple 
  by mocking the factory.
- **UI Renderer:** A pure function or class that transforms the internal **Data State** into a **UI State**. This separates the business logic of "what to do" 
  from the presentation logic of "what to show."
- **State Context:** A common interface passed to all states in a machine. It provides access to shared tools like the State Factory, UI Renderer, 
  and Feature Hosts, reducing constructor boilerplate.
- **Data State:** A simple data class that holds the cumulative state of the flow (e.g., entered username, password, validation errors). It is passed between 
  states to maintain continuity.
