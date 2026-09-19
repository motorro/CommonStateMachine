# Module implementation: basic views
Use this reference to implement the basic views for the implementation.

## Step 1. Implement scaffolding screen
Create the scaffolding composable that will draw all the [UI States](data-and-ui-implementation.md#step-2-ui-states):

- Follow common design system and approach in a project if available (scaffolds, appbars, themes, dimensions, etc.).
- If no design system found use the following approach:
  - Use `Scaffold` as a top-level element
  - Create top app bar with a module title
- The function should accept the following parameters:
  - Required: `uiState` - one of the [UI State implementation](data-and-ui-implementation.md#step-2-ui-states) will be passed.
  - Required: `onGesture` - a callback accepting [Gesture implementation](data-and-ui-implementation.md#step-1-ui-gestures). Make this parameter the last one.
  - Optional: `modifier` with a default value in case this is a common approach in current project.
- Place `when` operator in the content section and implement all branches of the UI states.
- All functions should be internal at most

See the [example](../assets/example/implementation/ui/AuthScreen.kt) for our sample authentication project.

## Step 2. Implement child views
Create a separate file for child views. Create a file per each [UI State](data-and-ui-implementation.md#step-2-ui-states):

- Follow common design system and approach in a project if available (scaffolds, appbars, themes, dimensions, etc.).
- The function should accept the following parameters:
  - Required: `uiState` - the concrete [UI State implementation](data-and-ui-implementation.md#step-2-ui-states) will be passed.
  - Required: `onGesture` - a callback accepting [Gesture implementation](data-and-ui-implementation.md#step-1-ui-gestures). Make this parameter the last one.
  - Required: `modifier` with a default value.
- If no design system found use the following approach:
  - Each screen should take `fillMaxSize()` to occupy the available space.
  - Center content both horizontally (`Alignment.CenterHorizontally`) and vertically (`Arrangement.Center` for `Column` or `Alignment.Center` for `Box`).
  - Use common project padding if defined in the design system (e.g., `Dimensions.medium`) for the main container.
  - Use design system theme or `MaterialTheme` for colors and typography to ensure consistency.
- Evaluate each UI state and create corresponding widgets
- Use `onGesture` to pass user gestures. Consider moving the lambas in `onChange` callbacks to `remember` to optimize recompositions if you find it effective.
- Generate previews. If you think multiple previews are needed - use data providers.
- Put a child view to the `when` branches of a scaffolding screen.

See the examples:

- [Loading view](../assets/example/implementation/ui/AuthLoadingScreen.kt)
- [Form view](../assets/example/implementation/ui/AuthFormScreen.kt)
- [Error view](../assets/example/implementation/ui/AuthErrorScreen.kt)
