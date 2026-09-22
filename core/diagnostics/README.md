# `:core:diagnostics`

Typed, privacy-safe diagnostic events and their text formatter.

The module deliberately has no generic `log(message)` API. Callers must choose a typed event so a
phone number, contact name, call note or exception message cannot be added accidentally. Android
storage and clipboard handling remain platform concerns in `:app`.
