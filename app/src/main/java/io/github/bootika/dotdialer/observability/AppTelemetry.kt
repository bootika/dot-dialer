package io.github.bootika.dotdialer.observability

import android.content.Context
import dev.goodwy.rphone.BuildConfig
import io.github.bootika.dotdialer.core.diagnostics.DiagnosticEvent
import io.github.bootika.dotdialer.core.diagnostics.DiagnosticFormatter
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/** Privacy-constrained Sentry integration for crashes, ANRs and typed lifecycle breadcrumbs. */
object AppTelemetry {
    private const val BREADCRUMB_CATEGORY = "dot_dialer.lifecycle"
    private const val TEST_EVENT_NAME = "dot_dialer_manual_test"
    private const val FLUSH_TIMEOUT_MILLIS = 3_000L

    private val initialized = AtomicBoolean(false)
    private val enabled = AtomicBoolean(false)

    val isConfigured: Boolean
        get() = BuildConfig.SENTRY_DSN.isNotBlank()

    val isEnabled: Boolean
        get() = enabled.get() && Sentry.isEnabled()

    fun initialize(context: Context) {
        if (!isConfigured || !initialized.compareAndSet(false, true)) return

        try {
            SentryAndroid.init(context.applicationContext) { options ->
                options.dsn = BuildConfig.SENTRY_DSN
                options.environment = if (BuildConfig.DEBUG) "test" else "production"
                options.release = buildReleaseName()
                options.dist = BuildConfig.VERSION_CODE.toString()

                options.isSendDefaultPii = false
                options.isAttachScreenshot = false
                options.isAttachViewHierarchy = false
                options.isEnableUserInteractionBreadcrumbs = false
                options.isEnableUserInteractionTracing = false

                // Start with errors and ANRs only. Tracing/replay can be enabled later with a budget.
                options.sampleRate = 1.0
                options.tracesSampleRate = 0.0
                options.beforeSend = SentryOptions.BeforeSendCallback { event, _ ->
                    SentryPrivacyFilter.scrub(event)
                }
            }
            enabled.set(Sentry.isEnabled())
        } catch (_: Exception) {
            // Telemetry must never prevent a phone app from starting.
            enabled.set(false)
        }

        if (!isEnabled) return
        Sentry.setTag("distribution", BuildConfig.FLAVOR)
        Sentry.setTag("build_type", BuildConfig.BUILD_TYPE)
    }

    fun addBreadcrumb(event: DiagnosticEvent) {
        if (!isEnabled) return

        Sentry.addBreadcrumb(
            Breadcrumb().apply {
                category = BREADCRUMB_CATEGORY
                level = SentryLevel.INFO
                message = DiagnosticFormatter.format(event)
            }
        )
    }

    suspend fun captureTestEvent(): String? = withContext(Dispatchers.IO) {
        if (!isEnabled) return@withContext null

        val eventId = Sentry.captureMessage(TEST_EVENT_NAME, SentryLevel.WARNING)
        Sentry.flush(FLUSH_TIMEOUT_MILLIS)
        eventId.toString()
    }

    private fun buildReleaseName(): String =
        "${BuildConfig.APPLICATION_ID}@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}"
}
