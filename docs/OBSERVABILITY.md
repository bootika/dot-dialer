# Observability and Sentry

Dot Dialer uses a dedicated project in the `dot-design-web` Sentry organization:

- project: `dot-dialer`;
- test environment: `test`;
- production environment: `production`;
- release format: `<applicationId>@<versionName>+<versionCode>`;
- distribution: Android `versionCode`.

## Configuration

The app reads the public ingest DSN from either the `SENTRY_DSN` environment variable or the
ignored project-local `local.properties` file:

```properties
SENTRY_DSN=https://public-key@organization.ingest.sentry.io/project-id
```

A blank DSN disables Sentry completely. `SENTRY_AUTH_TOKEN` is never compiled into the app and must
never be committed. GitHub Actions receives the DSN through the repository secret named
`SENTRY_DSN`.

## Privacy boundary

`AppTelemetry` enables crash and ANR reporting while explicitly disabling:

- default PII collection;
- screenshots and view hierarchy attachments;
- automatic UI interaction breadcrumbs and tracing;
- performance tracing;
- session replay.

Before every event leaves the device, `SentryPrivacyFilter` removes the user, request, server/device
name, device identifier, extras, exception messages and arbitrary breadcrumbs. Stack traces, app
version, Android version, device manufacturer/model and typed Dot Dialer lifecycle breadcrumbs are
retained. Phone numbers, contact names, notes and call contents must never be added to tags or
contexts.

## Verification

The Diagnostics screen shows whether Sentry initialized successfully. **Send Sentry test event**
queues a deliberately content-free warning and displays its event ID. Confirm that exact ID in the
Sentry `test` environment before treating the integration as operational. Initialization fails
closed and never prevents the dialer from starting.

Sentry cannot observe an Android force-stop itself. It can capture the preceding crash or ANR;
Dot Dialer's private on-device journal covers lifecycle reconstruction after the app is reopened.

Release builds are minified. ProGuard/R8 mapping upload must be configured and verified before the
first public release; the debug test APK is not obfuscated and does not require a mapping file.
