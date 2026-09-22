package io.github.bootika.dotdialer.core.call

object CallPresentationPolicy {
    const val DEFAULT_ALWAYS_FULL_SCREEN = true
    const val OUTGOING_SESSION_GRACE_MILLIS = 3_000L

    fun shouldLaunchCallActivity(
        isIncomingRinging: Boolean,
        alwaysFullScreen: Boolean = DEFAULT_ALWAYS_FULL_SCREEN,
    ): Boolean = !isIncomingRinging || alwaysFullScreen

    fun shouldFinishBeforeRendering(
        telecomInCall: Boolean?,
        repositoryHasActiveCall: Boolean,
        pendingOutgoingLaunch: Boolean,
    ): Boolean = !pendingOutgoingLaunch &&
        (telecomInCall == false || (telecomInCall == null && !repositoryHasActiveCall))

    fun missingSessionDismissDelayMillis(awaitingOutgoingSession: Boolean): Long =
        if (awaitingOutgoingSession) OUTGOING_SESSION_GRACE_MILLIS else 400L
}
