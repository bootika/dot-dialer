# Dot Dialer test APK

The FOSS debug build is intentionally separate from RDialer and from a future production build:

- app label: `Dot Dialer Test`;
- application ID: `io.github.bootika.dotdialer.test`;
- minimum Android version: Android 10 (API 29);
- output: `app/build/outputs/apk/foss/debug/`.

It can be installed beside RDialer. Android allows only one default phone app at a time, so testing
real incoming and outgoing calls requires temporarily selecting Dot Dialer Test as the default
phone app.

## Install

Open the APK on the phone and allow installation from that source, or use ADB:

```shell
adb install -r app/build/outputs/apk/foss/debug/dot-dialer-52-foss-debug.apk
```

The `52` segment is the current version code and changes when the project version is incremented.

If Android reports a signature mismatch after switching between a local and a CI-built APK,
uninstall only `Dot Dialer Test`, then install the new APK. Its settings and local diagnostic log
will be removed by that uninstall.

## Capture diagnostics

1. Open **Settings > Other > Diagnostics** in Dot Dialer Test.
2. Reproduce the problem. Include rapid open/close, answer/end and force-stop scenarios when they
   are relevant.
3. Return to **Diagnostics** and tap **Refresh**.
4. Tap **Copy diagnostics**, review the text and paste it into the bug report.

The journal is stored only in the app's private storage, is excluded from Android backup and is
bounded to 256 KB. It records lifecycle states and coarse call-state transitions. It does not log
phone numbers, contact names, call notes or exception messages.

## First physical-device pass

Check these in order so a failure is easy to isolate:

1. install beside RDialer and launch;
2. grant and revoke requested permissions;
3. open contacts, recents, keypad, settings and diagnostics repeatedly;
4. select Dot Dialer Test as the default phone app;
5. place, answer, reject and end one call at a time;
6. repeat with rapid navigation, screen lock/unlock and app force-stop;
7. if available, repeat with a second SIM, Bluetooth and Android Auto;
8. copy the diagnostic report immediately after any stuck-call state.

The Gradle quality gate verifies compilation, JVM tests, instrumented-test compilation, lint and
APK assembly. Carrier behavior and manufacturer-specific Telecom integration remain unverified
until this physical-device pass is completed.
