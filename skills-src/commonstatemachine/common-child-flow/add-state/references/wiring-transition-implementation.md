# Module implementation: wiring a transition from an existing state
Use this reference when the user told you how an existing machine state leads to the state you just
created. You will modify the EXISTING (calling) state, not the new one.

Take a look at the [class diagram](common-child-flow.puml) to get the idea about the components'
purpose.

Per Navigation Inversion (see [ARCHITECTURE.md](ARCHITECTURE.md)), a transition between logical
states doesn't need a user in the loop at all - the machine moves itself forward whenever a state
decides it's done. There are two patterns below; figure out which one the user actually means before
touching any code.

## Pattern A: automatic handoff (no gesture)
This is the common shape for a chain of invisible steps - for example "loads a token, then checks
it's still valid, then loads the profile". None of this is something the user clicks through: each
state's own `doStart` runs its work and, once it completes, calls the next state's factory method and
switches to it - exactly like
[PreloadingState](../assets/example/implementation/state/PreloadingState.kt) already does (see [state-implementation.md, Example 1](state-implementation.md#example-1-preloading-state)): it
preloads data in `doStart` and, on success, creates the next state with the factory and calls
`setMachineState` - no gesture anywhere in the sequence.

To wire this into an existing state:

1. Find where its startup work currently finishes successfully (the end of `doStart`, or a
   callback/coroutine completion inside it).
2. At that point, extract whatever data the new state needs - from the existing state's own
   interstate data, or from whatever the just-finished work produced.
3. Call the [state-factory method](renderer-factory-context-interface-implementation.md#step-2-state-factory-interface)
   created for the new state and switch to it with `setMachineState`, using the convenience method
   from the [base state](base-state-implementation.md#step-2-add-convenience-methods-to-change-the-machine-and-ui-states).
4. Only call `setUiState` along the way if something actually needs to change on screen - a chain of
   invisible steps can keep showing the same `Loading` UI-state the whole way through.

A recoverable-error variant of the same idea - switching back to an earlier state once some condition
is met, rather than forward to a new one - is shown by [PreloadingErrorState](../assets/example/implementation/state/PreloadingErrorState.kt), which
switches back to the loading state when the error is considered recoverable (see [state-implementation.md, Example 2](state-implementation.md#example-2-preloading-error-state)).

## Pattern B: triggered by a gesture
This is the case when the user's own action moves the flow forward - for example "when the user
clicks a friend in the list, navigate to the friend's details".

1. Find or add the triggering gesture: if it already exists (for example a `SelectItem` gesture
   carrying the clicked item), reuse it; otherwise add a new gesture case to the sealed Gesture
   structure, following [this guide](data-and-ui-implementation.md#step-1-ui-gestures), and handle it in the existing state's gesture processing.
2. In that gesture's handling, extract whatever data the new state needs (from the gesture, or from
   the existing state's own interstate data), call the new state's
   [state-factory method](renderer-factory-context-interface-implementation.md#step-2-state-factory-interface), and switch to it with `setMachineState`.

Take a look at the [example](../assets/example/implementation/state/FormState.kt): it already demonstrates this exact pattern - it changes the machine 
state when its action gesture is receive (see [state-implementation.md, Example 3](state-implementation.md#example-3-form-state)).

## Update the existing state's test
Whichever pattern applies, add a test case to the existing state's unit test that:

- For Pattern A: drives the mocked startup dependency (use-case, flow, etc.) to complete, without
  sending any gesture.
- For Pattern B: sends the triggering gesture (with fixture data if it carries any).
- Either way: asserts the state-factory method was called with the expected arguments, and that the
  machine state was switched to the state the factory returned.

Follow [the state-test guide](state-test-implementation.md#step-3-testing-gestures) for how to
structure a test like this - for Pattern A, the equivalent of "sending a gesture" is whatever already
drives the state's startup behaviour in its other tests.
