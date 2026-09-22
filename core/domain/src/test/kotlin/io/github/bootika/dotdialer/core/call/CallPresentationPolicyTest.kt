package io.github.bootika.dotdialer.core.call

import org.junit.Assert.assertEquals
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

    @Test
    fun `foreground outgoing launch survives the telecom registration race`() {
        assertFalse(
            CallPresentationPolicy.shouldFinishBeforeRendering(
                telecomInCall = false,
                repositoryHasActiveCall = false,
                pendingOutgoingLaunch = true,
            )
        )
        assertTrue(
            CallPresentationPolicy.shouldFinishBeforeRendering(
                telecomInCall = false,
                repositoryHasActiveCall = false,
                pendingOutgoingLaunch = false,
            )
        )
        assertEquals(
            3_000L,
            CallPresentationPolicy.missingSessionDismissDelayMillis(awaitingOutgoingSession = true),
        )
        assertEquals(
            400L,
            CallPresentationPolicy.missingSessionDismissDelayMillis(awaitingOutgoingSession = false),
        )
    }
}
