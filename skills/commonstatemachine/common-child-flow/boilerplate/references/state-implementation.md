# Module implementation: state implementation
Use this reference to implement the machine-state for the `CommonStateMachine` framework. 
Take a look at the [class diagram](../assets/common-child-flow.puml) to get the idea about components purpose.
Take a look at the [example](../assets/example/implementation/state/PreloadingState.kt).

## Mechanics
Here's how the machine-state works:

- All machine states subclass the basic `com.motorro.commonstatemachine.CommonMachineState` (or its child) bound with the gesture and UI-state types
  as described in the basic [interface implementation](renderer-factory-context-interface-implementation.md#step-1-commonmachinestate-binding) 
  and [base state implementation](base-state-implementation.md) manuals.
- It'd be great if a state has a single responsibility whenever possible and limit the number of gestures it processes.
  For example, the document loading could be divided into the following states:
  - `LoadingState` - runs the usecase and updates the interstate data.
  - `ContentState` - displays loaded data and interacts with a user.
  - `ErrorState` - displays an error and if possible, offers to retry by transferring back to `LoadingState`.
- The state has the following callbacks which are called by the hosting state machine:
  - `doStart` - called when the state is started.
  - `doProcess` - called when the state receives a gesture.
  - `doClear` - called when the state is removed from the state-machine.
- The state could call the following functions to interact with the hosting state machine:
  - `setMachineState` - called to switch to another state. We tipically use the factory to create the new state.
  - `setUiState` - called to update the UI state. We tipically use the renderer to update the UI.
- It is important that `setMachineState` and `setUiState` are called from when state is active (between calls to `doStart` and `doClear`). 
  If the call is made from the inactive state - it will fail.
- Always postpone the heavy initialization till `doStart` is called.

## Step 1. Create a class
Put the class to the `state` sub-package of the implementation module.
Follow the following common rules:

- Make the class internal to the implementation module.
- Subclass the [base state](base-state-implementation.md).
- Pass the [context](renderer-factory-context-interface-implementation.md#step-4-state-context) as a first parameter.
- Pass the [input data](input-and-result-api.md#step-1-input) as a parameter.

## Step 2. Add a startup implementation (or a dummy)
When the `com.motorro.commonstatemachine.CommonMachineState` is started, the state-machine calls the `doStart` lifecycle method. 
Implement the method to update the UI, start loading data, etc.
The `com.motorro.commonstatemachine.coroutines.CoroutineState`, which is an ancestor of the [abstract base state](base-state-implementation.md), has a 
bound coroutine scope property: `stateScope`. Use it to run coroutines. The scope would be canceled when the state is removed from the state-machine.

## Step 3. Add a gesture processing implementation
When the user interface emits a gesture, the state-machine calls the `onGesture` method of a current `com.motorro.commonstatemachine.CommonMachineState`.
Implement the override of a function that:

- Will process the gestures specific to this state.
- Will call the super-method on every other unexpected gesture which are out of scope of this state.

## Step 4. Implement a local state factory
To increase the visibility and to decrease the boilerplate, the states that require external dependencies (use-cases, managers, APIs, etc.) have their
own state factories put to the DI graph. This lifts a burden of state creation micromanagement away from
the main state factory implementation.
Create such a factory for the startup state:

- Implement as a child class with the name `Factory`.
- If required by the DI framework used in project, add required inject annotations.
- Inject external dependencies required by the state to the factory constructor.
- Add a single `create` method that will accept the [context](renderer-factory-context-interface-implementation.md#step-4-state-context) and all the required interstate parameters that will come from the main factory.

## Step 5. Create a method in the main state-factory interface
The states are to be created by the [main state-factory](renderer-factory-context-interface-implementation.md#step-2-state-factory-interface) so the states 
do not have hard links to one each-other.
When you create a new state, you need to create a method in the main state-factory for it:

- Name the function to indicate what happens inside the state. Examples: `preloading`, `error`, etc.
- Pass the common interstate if needed as a data parameter (see the `form` function in the [example](../assets/example/implementation/state/AuthStateFactory.kt)).
- Pass the specific parameters - those needed by this particular state only (see the `preload` function in the [example](../assets/example/implementation/state/AuthStateFactory.kt)).
- Implement the method in the [implementation class](renderer-factory-context-interface-implementation.md#step-5-state-factory-implementation).

## Example 1. Preloading state
For example of a state that preloads some data with the usecase, take a look at the [PreloadingState](../assets/example/implementation/state/PreloadingState.kt) class.
The state does the following when started:

- Sets the `Loading` UI state in `doStart` method.
- Calls the `preload` function that:
  - calls a usecase to preload data some data or emulates loading if the usecase is not specified, and we are doing a dummy preloading.
  - creates the valid [data-state](data-and-ui-implementation.md#step-3-interstate-data).
  - switches to the next state by creating it with a factory and calling the `setMachineState` function.
- Implements the `doProcess` function to handle the back gesture or any other gestures that are relevant to this state.
- Does moderate debug-logging in lifecycle methods to track the flow.

## Example 2. Preloading error state
If any operation fails, you'd better switch the state-machine to  an error state. See [PreloadingErrorState](../assets/example/implementation/state/PreloadingErrorState.kt) 
state to handle the preloading error.
The typical error state would:

- Set the `Error` UI state in `doStart` method passing the error to display
- Handle the back gesture that would terminate the flow or return to a previous state if relevant keeping the variables needed.
- May switch back to the loading state if the error is considered recoverable.
- States of that kind are usually rather simple so you don't need any state-factory.

### Example 3. Form state
For example of a state that handles the user input, validates and prepares the data, take a look at the [FormState](../assets/example/implementation/state/FormState.kt) class.
The state does the following when started:

- Updates the UI state with the initial data.
- Listens to the gestures, changes the interstate data and updates the screen in the `doProcess` method.
- Changes the machine state when the action gesture is received.
- Validates the data


