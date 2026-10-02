# Module implementation: test fixtures
Use this reference to create test fixtures for the [data classes of the feature implementation](data-and-ui-implementation.md):

1. Create a `data` sub-package in the test source set.
2. Create a fixture file for data-class gestures.
3. Create some ui-state fixture to use when mocking the [renderer](renderer-factory-context-interface-implementation.md#step-3-ui-renderer-interface).
4. Create a fixture file for data-class interstate data, input and result data classes.
   
For the fixture data, scan the project for already defined fixtures available and prefer using them instead of creating 
your own fixtures. For example, look for the modules like `domain` or `entity` and use their fixtures. Check existing feature 
modules for the clue how to create fixtures and where to get the common fixture module.

Take a look at the [example](../assets/example/implementation-tests/data/authGesturesFixtures.kt) gesture fixtures.
Take a look at the [example](../assets/example/implementation-tests/data/uiStatesFixtures.kt) ui-state fixtures.
Take a look at the [example](../assets/example/implementation-tests/data/dataStatesFixtures.kt) interstate.
