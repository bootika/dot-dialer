# Testing Dot Dialer

The project treats automated testing as a release gate. A passing compilation without executed
tests is not a valid verification result.

## Local foundation check

Set `ANDROID_HOME` or create `local.properties` with `sdk.dir`, then run:

```shell
./gradlew :app:compileFossDebugKotlin \
  :app:testFossDebugUnitTest \
  :app:compileFossDebugAndroidTestKotlin \
  :app:lintFossDebug
```

On Windows use `gradlew.bat` and the same task names.

## Test layers

- `app/src/test`: fast JVM tests for call lifecycle, haptic policy, search, backup and domain rules.
- `app/src/androidTest`: Compose semantics, accessibility, Android integration and UI flows.
- Future `macrobenchmark` module: startup and end-user performance scenarios.
- Future external device harness: force-stop, process death, permission changes and Telecom flows.

## Critical rules

- Use virtual time for delayed or coroutine-driven behavior; do not use sleeps in unit tests.
- A finished call can never become active again.
- An event from an old session can never mutate the current session.
- Cleanup from an old call can never remove a newer or waiting call.
- No active call means no ongoing notification, floating call UI or call activity.
- Test data must not contain personal user data.
- Every production regression receives a failing test before the fix where technically possible.
- Retries may gather diagnostics but must not convert the original failed run to success.

## Reports

JUnit and lint reports are generated below `app/build/reports` and `app/build/test-results`.
CI uploads these directories even when the quality gate fails.

## Current verified baseline

The foundation currently has 16 JVM tests covering call lifecycle and haptic policy, plus one
instrumented Compose accessibility test. The instrumented test has been executed on Android API
36. Real carrier calls, multi-SIM behavior, Bluetooth routing and OEM-specific Telecom behavior
still require the external device harness and physical-device coverage described above.
