# Accessibility and haptics

## Product rule

Accessibility must improve the existing interface rather than add explanatory visual clutter.
Prefer correct component semantics, localized labels, predictable focus order, scalable text,
adequate contrast and touch targets of at least 48dp.

Decorative images use a null content description. Actionable icons use a short localized label.
Composite rows should expose one useful semantic node unless their child actions need separate
focus. State changes use state descriptions or live regions only when the user needs immediate
feedback.

## Haptic rule

Haptics communicate an interaction or state change; they are not decoration. Frequent effects,
such as keypad and scroll ticks, stay short and subtle. Confirmation and rejection may be more
distinct. The application setting and the Android system touch-feedback setting are both honored.

New code uses semantic intents from `core/haptics` and the `AndroidHaptics` boundary. It must not
add direct vibrator calls to screens or composables.

## Verification

- Compose semantics tests for labels, click actions, state and minimum targets.
- Automated accessibility checks on the critical screens.
- Font scale and display-size matrix on emulators.
- TalkBack and Switch Access pass before a public release.
- Physical-device review for haptic quality; emulator execution alone cannot judge feel.

An accessibility test passing does not prove the entire flow is usable with a screen reader. The
release checklist retains a manual pass for the main call, contact, recents and settings flows.
