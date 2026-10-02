---
name: commonstatemachine-commonchildflow-add-state
description: Use this skill to add one more logical state to a feature-implementation module that
  already exists and follows the CommonStateMachine Common Child Flow design. A new state doesn't
  have to be a new screen - it can be an invisible step, like an auth check or a data-prep step,
  that moves the flow forward while the UI keeps showing what it already shows. Use it whenever the
  user asks to add a new screen, step or state to an existing flow - for example "Add a details
  screen to the friend list module", "Add a change-password state to the auth flow", or "Add a step
  that checks the session before the content screen". If the module doesn't exist yet, use the
  boilerplate skill instead.
metadata:
  author: Motorro
  last-updated: '2026-10-02'
  keywords:
  - android
  - architecture
  - cross-platform
  - kotlin-multiplatform
  - mvi
  - state-machine
  - feature-flow
---

## Description
Use this skill to add one more logical state to an existing feature-implementation module built with
the [Common Child Flow](references/COMMON-CHILD-FLOW.md) design (see the boilerplate skill that
creates such a module in the first place). This skill does not create a new module - it extends an
existing one with a new logical state, the UI to show it (if it needs one), and (optionally) the transition
that leads to it from a state already in the module.

A logical state is not necessarily a new screen. As the [Core Architecture](references/ARCHITECTURE.md)
describes under Navigation Inversion, the user doesn't navigate between screens - the machine moves
from one logical state to another, either on a gesture or simply because a state's own work finished,
and whichever state is active decides what the UI shows. Several states can run one after another
while the UI keeps showing the very same thing, with nothing visibly changing until some state
actually calls `setUiState` with something new. Keep this in mind while gathering requirements below:
"runs a use-case, then checks the session, then prepares some data" can just as easily be three new
logical states as a single one, with the UI showing one loading spinner throughout.

For a deeper dive into the state machine mechanics and common components, refer to the
[Core Architecture](references/ARCHITECTURE.md).
For a deeper dive into the common child flow philosophy and design, refer to the
[Common Child Flow Architecture](references/COMMON-CHILD-FLOW.md).

## Example
This instruction uses the same [example authentication module](assets/example) as the boilerplate
skill. All examples and code samples refer to it. Adjust the names and packages to your actual need.

## Gathering the requirements for the new state
Before changing any code, work out what the new state needs from what the user gave you:

- A screen design (a Figma link/export, or a picture): inspect it to infer the UI-state's visual
  elements (text, inputs, lists, buttons) and the gestures the user can perform on it (taps, input
  changes, navigation). Name things after what the design actually shows rather than guessing.
- A state description in words (for example: "runs a use-case, then shows the result; let the user
  retry on error", or "checks if the user is still authenticated, then moves on"): infer the
  gesture(s) if any, what the state does on startup (which use-case or data it needs), whether it
  carries data forward to or from its neighbours, and whether it needs a UI-state of its own at all -
  an invisible step can simply keep showing whatever its predecessor already set.
- If the user describes a chain of steps ("loads a token, then checks it's still valid, then loads
  the profile"), that is usually several new logical states handed off to one another automatically,
  not one state doing everything - see Step 6 below for how a state hands off to the next one without
  any user gesture involved.
- If the user gave you both, or only part of this, use what you have and ask clarifying questions
  about anything still ambiguous (what happens on error, what data is required, etc.) rather than
  guessing silently.

Follow these common rules when writing code:

- Follow the project coding conventions or use Kotlin default.
- Follow the project naming conventions or use Kotlin default.
- If the project uses KDoc as a coding convention - add it to the created types, methods, properties.
- Add TODO comments in dummy classes and functions to point the user to provide the actual
  implementation.
- Don't hesitate to ask the user to clarify what they need.

## Step 1. Extend the gesture, UI-state and interstate data
The new state may need a gesture and a UI-state of its own, and it may need to carry extra data
between states - but not always: a state with no user input needs no new gesture, and a state that
doesn't change what's on screen can just keep reusing an existing UI-state.

Follow [this guide](references/data-and-ui-implementation.md) to:

1. Add the gesture case(s) for the new state to the existing sealed Gesture structure, if it needs
   one (Step 1).
2. Add the UI-state case for the new state to the existing sealed UI-state structure, if it needs
   one (Step 2).
3. Add properties to the interstate data if the new state needs to pass data to or from its
   neighbours (Step 3).
4. Extend the test fixtures for whatever you added above (Step 4).

## Step 2. Create the new machine state
Follow [this guide](references/state-implementation.md) to create the state itself:

1. Subclass the [base state](references/base-state-implementation.md) (Step 1).
2. Implement its startup behaviour in `doStart` - run the use-case, update the UI state, etc (Step 2).
3. Implement gesture processing for the gesture(s) added in Step 1 above, if any (Step 3).
4. If the state needs external dependencies, add a local state factory for it (Step 4).

## Step 3. Add a state-factory method
Create a method on the main state factory and implement it, so other states (including the one you
may change in Step 6) can create the new state:

1. Add the method to the
   [state-factory interface](references/renderer-factory-context-interface-implementation.md#step-2-state-factory-interface).
2. Implement the method in the
   [state-factory implementation](references/renderer-factory-context-interface-implementation.md#step-5-state-factory-implementation),
   following [this guide](references/state-implementation.md#step-5-create-a-method-in-the-main-state-factory-interface).

## Step 4. Test the new state
Follow [this guide](references/state-test-implementation.md) to create a unit test for the new
state, based on the [base test class](references/base-test-class-implementation.md).

## Step 5. Implement the view
If the new state has a UI-state of its own, follow these guides to render it:

1. [Implement the UI renderer, if used](references/ui-renderer-implementation.md).
2. [Implement the composable view](references/ui-view-implementation.md) for the new UI-state.

## Step 6. Wire the transition from another state (if instructed)
If the user told you how the flow reaches this new state from an existing one, the existing state
that leads to it needs to change too. This happens one of two ways:

- **Automatically, when the existing state's own work finishes** - for example "once the session
  check passes, load the profile". Nothing here is something the user clicks through: the existing
  state's `doStart` already runs that work, and it just needs to call the new state's factory method
  and switch to it once that work completes, with no gesture involved. This is the common shape for a
  chain of invisible steps.
- **On a gesture** - for example "when the user clicks a friend in the list, navigate to the friend's
  details". The existing state needs to handle (or already handles) that gesture and switch states in
  response to it.

Follow [this guide](references/wiring-transition-implementation.md) for both cases:

1. For an automatic handoff, find where the existing state's startup work completes; for a
   gesture-driven one, add or reuse the triggering gesture in the existing state's gesture
   processing.
2. Call the state-factory method from Step 3 and switch to the new state.
3. Update the existing state's unit test to cover the new transition.

If the user didn't say how the state is reached, skip this step and tell the user that nothing
transitions into the new state yet.
