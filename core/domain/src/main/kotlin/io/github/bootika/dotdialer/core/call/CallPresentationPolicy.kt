package io.github.bootika.dotdialer.core.call

object CallPresentationPolicy {
    const val DEFAULT_ALWAYS_FULL_SCREEN = true

    fun shouldLaunchCallActivity(
        isIncomingRinging: Boolean,
        alwaysFullScreen: Boolean = DEFAULT_ALWAYS_FULL_SCREEN,
    ): Boolean = !isIncomingRinging || alwaysFullScreen
}
