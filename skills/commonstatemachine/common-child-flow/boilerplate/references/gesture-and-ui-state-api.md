# Module API: Gesture and UI-state
Use this reference to create the UI gesture and UI state API.

## Mechanics
The framework behind is a Model-View-Intent architecture, so:

- The state is updated as a result of user intents (gestures)
- The view-state is fully described by data classes/objects - ui-states

## Step 1. Gestures
The implementation is hidden from the module consumer using the internal gesture structures.
To use the feature we export the basic marker interface that may only contain some common public properties.
This interface would be implemented later in the implementation module.
Take a look at the [class diagram](common-child-flow.puml) for more details.
Take a look at the [example](../assets/example/api/AuthGesture.kt) - common API definition

## Step 2. UI-states
The implementation is hidden from the module consumer using the internal ui-state structures.
To use the feature we export the basic marker interface that may only contain some common public properties.
This interface would be implemented later in the implementation module.
Take a look at the [class diagram](common-child-flow.puml) for more details.
Take a look at the [example](../assets/example/api/AuthUiState.kt) - common API definition

