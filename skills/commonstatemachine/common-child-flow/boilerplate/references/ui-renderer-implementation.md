# Module implementation: renderer implementation
Use this reference to implement the [UI-renderer](renderer-factory-context-interface-implementation.md#step-4-ui-renderer-interface) (if used in the module).

## Step 1. Implement renderer
Follow these steps:

- Create the implementation class in the `ui` package of the implementation module and add the UI-renderer interface to the list of implementations.
- Enumerate all [UI-states](data-and-ui-implementation.md#step-2-ui-states) and check the source UI-renderer interface.
  Find the UI-states corresponding to renderer methods.
- Implement the interface methods transforming the passed parameters to the respectful states.
- Renderer may require other dependencies - resource mappers, etc. Make the class injectable using the DI framework used in a project.

Take a look at [example](../assets/example/implementation/ui/AuthUiRendererImpl.kt) for our Auth application.

## Step 2. Create a renderer test
Sometimes UI has a complex model that requires a lot to render.
Secure the logic by writing a unit-test for the renderer.

- Check every interface method
- Use [fixtures](test-fixtures-implementation.md) if available, modify them by copy-constructors if needed, or add custom.

Take a look at the [example](../assets/example/implementation-tests/ui/AuthUiRendererImplTest.kt).