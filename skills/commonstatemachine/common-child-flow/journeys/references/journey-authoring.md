# Writing and running Android Journeys

[Android Journeys](https://developer.android.com/studio/preview/journeys) (`com.android.tools.journeys:journeys-junit-engine`)
is an AI-driven UI-testing framework: instead of view-id locators or Espresso scripts, a journey file describes
what to do and check in plain-language steps. At each step the test runner captures a screenshot and the current
UI hierarchy, sends both to Gemini along with your instruction, and Gemini decides and performs the matching
action (tap, type, swipe, or an assertion) until the step's condition is met.

This is what makes the UI-flow guessing step in the main guide matter: because a step is matched by Gemini
against what's actually rendered, the journey's wording has to use the screen's real labels and real
conditions (the exact button text, the exact field label, whether a control is enabled) - not invented ones or
internal identifiers.

## Journey file format

A `.journey.xml` file has a `name`, a `description`, and an ordered list of `action` steps written as complete
sentences, in present tense, one user-observable action or check per step:

```xml
<?xml version="1.0" encoding="utf-8"?>
<journey name="Auth: Happy authentication">
    <description xml:space="preserve">Test authentication happy path with valid credentials</description>
    <actions xml:space="preserve">
        <action>Wait for the authentication form to open.</action>
        <action>Type "user" into the username field and "password" into the password field.</action>
        <action>Tap the Login button to submit the form.</action>
        <action>Verify that authentication succeeds and the screen displays "authenticated: true".</action>
    </actions>
</journey>
```

Keep each file to a single scenario - a happy path and a validation-error case are two files, not one file with
branches - so a failure points at one specific thing that broke.

## Where the files live, and the suite that finds them

AGP scans for `.journey.xml` files inside `src/<suiteName>/`, where `<suiteName>` is literally the name given to
the `testOptions.suites.create(...)` block in the module's `build.gradle.kts` (see the main guide's Gradle-setup
step). This skill names the suite `journeys`, so the files live under `src/journeys/` - if that name ever
changes, the folder has to move with it.

## Running a suite

```bash
./gradlew :path:to:the:journey:module:journeysDefault
```

(`journeysDefault` because the suite is named `journeys` and its target is named `default` - adjust both parts
to match whatever this project's module and suite are actually called.) To run a single file instead of the
whole suite:

```bash
JOURNEYS_FILTER=happy_path.journey.xml ./gradlew :path:to:the:journey:module:journeysDefault
```

## One-time setup: Gemini authentication

The `journeys-junit-engine` runner talks to Gemini via Google Application Default Credentials, not an API key
in the project. This is a one-time environment setup, not something this skill's generated files need to carry:

- **Android Studio**: Settings/Preferences -> Tools -> Gemini -> sign in, and enable Journeys under Studio Labs
  if it's not already on.
- **Local terminal / CLI**: `gcloud auth application-default login` once per machine.
- **CI**: a service account with the Vertex AI User role, authenticated via `GOOGLE_APPLICATION_CREDENTIALS`
  or impersonation - see the project's own CI setup for the exact mechanism in use.

## Two gotchas worth knowing before a run

- **Emulator soft keyboard**: emulators treat the host's keyboard as physical hardware by default, which
  suppresses the on-screen keyboard. If Gemini expects to see and dismiss a soft keyboard that never appears,
  typing and keyboard-dismissal steps can misfire. Fix with
  `adb shell settings put secure show_ime_with_hard_keyboard 1`, or permanently in the AVD's `config.ini`
  (`hw.keyboard = no`).
- **Cost**: each action step sends a screenshot and UI-hierarchy payload to Gemini - roughly 1,500-2,500 tokens
  per step, so a 10-step journey is on the order of 20,000 tokens. Prefer specific, unambiguous action wording
  (fewer retries) and `JOURNEYS_FILTER` for iterating on one file instead of re-running the whole suite.
