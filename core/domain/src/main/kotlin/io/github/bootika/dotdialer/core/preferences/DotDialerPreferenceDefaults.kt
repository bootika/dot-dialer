package io.github.bootika.dotdialer.core.preferences

/**
 * Product defaults derived from the owner's Real Phone configuration backup.
 *
 * Only reusable UI and interaction preferences belong here. Runtime state,
 * account identifiers, entitlement flags, onboarding state and contact-specific
 * data must remain device/user data and are deliberately excluded.
 */
object DotDialerPreferenceDefaults {
    private val booleanDefaults = mapOf(
        "avatar_frame" to false,
        "direct_call_on_tap" to true,
        "dynamic_colors" to true,
        "liquid_glass_ui" to true,
        "pocket_mode_prevention" to true,
        "scroll_animation_enabled" to true,
        "show_recents_favorites" to true,
        "show_recents_filter_chips" to true,
        "tab_show_calls" to false,
        "tab_show_contacts" to false,
        "tab_show_dialpad" to false,
        "tab_show_settings" to false,
    )

    private val stringDefaults = mapOf(
        "tab_order" to "contacts,calls,dialpad,notes,favorites,settings",
        "theme_mode" to "auto",
    )

    private val intDefaults = mapOf(
        "custom_primary_color" to -8_411_657,
    )

    fun booleanFor(key: String): Boolean? = booleanDefaults[key]

    fun stringFor(key: String): String? = stringDefaults[key]

    fun intFor(key: String): Int? = intDefaults[key]
}
