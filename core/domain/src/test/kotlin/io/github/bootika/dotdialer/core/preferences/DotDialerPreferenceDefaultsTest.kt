package io.github.bootika.dotdialer.core.preferences

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DotDialerPreferenceDefaultsTest {
    @Test
    fun `uses the reusable interaction and appearance values from the owner preset`() {
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("direct_call_on_tap"))
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("dynamic_colors"))
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("liquid_glass_ui"))
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("pocket_mode_prevention"))
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("scroll_animation_enabled"))
        assertEquals(false, DotDialerPreferenceDefaults.booleanFor("avatar_frame"))
        assertEquals("auto", DotDialerPreferenceDefaults.stringFor("theme_mode"))
        assertEquals(-8_411_657, DotDialerPreferenceDefaults.intFor("custom_primary_color"))
    }

    @Test
    fun `uses the owner's single-home-tab navigation preset`() {
        assertEquals(false, DotDialerPreferenceDefaults.booleanFor("tab_show_calls"))
        assertEquals(false, DotDialerPreferenceDefaults.booleanFor("tab_show_contacts"))
        assertEquals(false, DotDialerPreferenceDefaults.booleanFor("tab_show_dialpad"))
        assertEquals(false, DotDialerPreferenceDefaults.booleanFor("tab_show_settings"))
        assertEquals(
            "contacts,calls,dialpad,notes,favorites,settings",
            DotDialerPreferenceDefaults.stringFor("tab_order"),
        )
    }

    @Test
    fun `keeps both optional Recents sections visible by default`() {
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("show_recents_filter_chips"))
        assertEquals(true, DotDialerPreferenceDefaults.booleanFor("show_recents_favorites"))
    }

    @Test
    fun `does not turn personal runtime state into product defaults`() {
        listOf(
            "favorites_order",
            "is_pro_iap",
            "is_pro_sub",
            "last_opened_tab",
            "last_used_account_name",
            "last_used_account_type",
            "onboarding_shown",
            "tab_show_setting",
        ).forEach { key ->
            assertNull(DotDialerPreferenceDefaults.booleanFor(key))
            assertNull(DotDialerPreferenceDefaults.stringFor(key))
            assertNull(DotDialerPreferenceDefaults.intFor(key))
        }
    }
}
