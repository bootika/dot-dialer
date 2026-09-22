package dev.goodwy.rphone.controller.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import io.github.bootika.dotdialer.core.haptics.HapticIntent
import io.github.bootika.dotdialer.core.haptics.HapticPolicy
import io.github.bootika.dotdialer.core.haptics.HapticStrength

/**
 * Single Android boundary for app haptics.
 *
 * Prefer [performOnView] for foreground interactions because Android then selects the device-
 * appropriate effect and honours the system touch-feedback setting. [performPulse] remains for
 * interactions without a View and explicitly checks that setting before using the vibrator.
 */
object AndroidHaptics {
    private const val TAG = "AndroidHaptics"

    fun performOnView(
        view: View,
        intent: HapticIntent,
        appEnabled: Boolean = true,
    ): Boolean {
        if (!appEnabled || !view.isHapticFeedbackEnabled) return false
        return view.performHapticFeedback(feedbackConstant(intent))
    }

    fun performPulse(
        context: Context,
        intent: HapticIntent,
        strengthValue: String,
        customIntensity: Float = 0.5f,
        appEnabled: Boolean = true,
    ): Boolean {
        if (!appEnabled || !systemTouchFeedbackEnabled(context)) return false

        val vibrator = vibrator(context) ?: return false
        if (!vibrator.hasVibrator()) return false
        val pulse = HapticPolicy.pulse(
            intent = intent,
            strength = HapticStrength.fromStorage(strengthValue),
            customIntensity = customIntensity,
        )

        return try {
            vibrator.vibrate(VibrationEffect.createOneShot(pulse.durationMs, pulse.amplitude))
            true
        } catch (error: SecurityException) {
            Log.w(TAG, "Haptic feedback was rejected by the system", error)
            false
        } catch (error: RuntimeException) {
            Log.w(TAG, "Haptic feedback failed", error)
            false
        }
    }

    private fun vibrator(context: Context): Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private fun systemTouchFeedbackEnabled(context: Context): Boolean = try {
        Settings.System.getInt(
            context.contentResolver,
            Settings.System.HAPTIC_FEEDBACK_ENABLED,
            1,
        ) != 0
    } catch (error: RuntimeException) {
        Log.w(TAG, "Could not read the system haptic setting", error)
        true
    }

    private fun feedbackConstant(intent: HapticIntent): Int = when (intent) {
        HapticIntent.TAP -> HapticFeedbackConstants.VIRTUAL_KEY
        HapticIntent.KEYBOARD -> HapticFeedbackConstants.KEYBOARD_TAP
        HapticIntent.CONFIRM -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            HapticFeedbackConstants.CONFIRM
        } else {
            HapticFeedbackConstants.VIRTUAL_KEY
        }

        HapticIntent.REJECT -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            HapticFeedbackConstants.REJECT
        } else {
            HapticFeedbackConstants.LONG_PRESS
        }

        HapticIntent.LONG_PRESS -> HapticFeedbackConstants.LONG_PRESS
        HapticIntent.SCROLL_TICK -> HapticFeedbackConstants.CLOCK_TICK
    }
}
