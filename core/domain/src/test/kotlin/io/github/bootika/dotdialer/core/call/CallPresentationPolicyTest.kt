package io.github.bootika.dotdialer.core.call

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CallPresentationPolicyTest {
    @Test
    fun `full screen is the default for incoming calls`() {
        assertTrue(
            CallPresentationPolicy.shouldLaunchCallActivity(isIncomingRinging = true)
        )
    }

    @Test
    fun `incoming calls respect an explicit compact preference`() {
        assertFalse(
            CallPresentationPolicy.shouldLaunchCallActivity(
                isIncomingRinging = true,
                alwaysFullScreen = false,
            )
        )
    }

    @Test
    fun `outgoing calls always open the call activity`() {
        assertTrue(
            CallPresentationPolicy.shouldLaunchCallActivity(
                isIncomingRinging = false,
                alwaysFullScreen = false,
            )
        )
    }
}
