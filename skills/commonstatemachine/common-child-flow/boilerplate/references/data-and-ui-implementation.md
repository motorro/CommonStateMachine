# Module implementation: data classes
Use this reference to create data classes for the feature implementation.
Take a look at the [class diagram](../assets/common-child-flow.puml) to get the idea about the data types within the module.

## Step 1. UI gestures
Create the sealed class to enumerate all the UI events (gestures) the user could send to the model:

- button presses
- navigation elements clicks
- input changes

The created sealed class should implement the base Gesture type defined in the API module.
See the [corresponding instruction](gesture-and-ui-state-api.md#step-1-gestures).
Put the created class to the `data` sub-package of the implementation module.

Follow these common rules:

- Make the created structure internal.
- Create data objects for gestures that don't carry any data. Example: user clicks the Back button.
- Create data classes for gestures that carry some data. Example: user changes an input text and the new value is passed with the event.## UI gestures.
- Always include the data object for the backwards navigation gesture: `Back`.
- If user didn't give you any information on the input gestures he wants, `Back` would be the only child object in a structure.
- If user gives you a picture of a user interface - inspect it and create gestures for every screen.
- If the feature includes multiple screens - group the specific gestures by screen.
- The types of the properties of the ui-gestures should be immutable!

Take a look at the [example](../assets/example/implementation/data/AuthGestureImpl.kt) - gesture collection.

## Step 2. UI states
Create the sealed class to enumerate all the UI states the interface could display.

The created sealed class should implement the base UI-state defined in the API module.
See the [corresponding instruction](gesture-and-ui-state-api.md#step-2-ui-states).
Put the created class to the `data` sub-package of the implementation module.

Follow these common rules:

- Make the created structure internal.
- Create data objects for ui-states that don't carry any data. Example: loading screen without any text messages.
- Create data classes for ui-states that carry some data. Example: login/password entry form.
- If user gives you a picture of a user interface - inspect it and create the data state for every dynamic UI element.
- If the feature includes multiple screens - group the specific ui-states by screen.
- The types of the properties of the ui-states should be immutable! Use `@Immutable` annotation for non-stable types to help Compose compiler optimize recompositions.
  If immutable collection library is used in a project - use them for the collections in UI-states.

Take a look at the [example](../assets/example/implementation/data/AuthUiStateImpl.kt) - UI-state collection.

## Step 3. Interstate data
Create a data class to store the flow data passed between the machine states. 
Follow the user's instructions to create a data class properties.
Put the created class to the `data` sub-package of the implementation module.

Follow these common rules:

- Make the created data class internal.
- The types of the properties of the data-state should be immutable!
- Include the flow startup input as a property.
- Set default values for the properties if user provides the information or try to guess them.

Take a look at the [example](../assets/example/implementation/data/AuthStateData.kt) - interstate data for password authentication.

## Step 4. Test fixtures
Create some fixtures and test data for the created data classes:

1. Create a `data` sub-package in the test source set.
2. Create a fixture file for data-class gestures.
3. Create a fixture file for data-class ui-states.
4. Create a fixture file for data-class interstate data. 
   For the interstate data, scan the project for already defined fixtures available at the class path and 
   prefer using them instead of creating your own fixtures


Take a look at the [example](../assets/example/implementation/data/AuthStateData.kt) - interstate data for password authentication.
