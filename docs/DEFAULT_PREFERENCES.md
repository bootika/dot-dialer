# Dot Dialer default preferences

Dot Dialer's product defaults are centralized in `DotDialerPreferenceDefaults`. The initial
preset was derived from the project owner's Real Phone backup on 2026-09-22.

Only reusable appearance, navigation and interaction choices were adopted. Existing stored values
always take precedence over the product defaults.

The following backup values are deliberately not product defaults:

- account identifiers;
- favorite contact IDs and ordering;
- purchase or subscription state;
- onboarding and other runtime state;
- the last opened screen.

Editable backup archives are an untrusted input. `PreferenceBackupPolicy` prevents entitlement
flags and authentication secrets from being exported or restored, and `BackupEntryPolicy`
prevents archived call notes from escaping their destination directory.
