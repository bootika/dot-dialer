package dev.goodwy.rphone

import android.app.Application
import android.os.Build
import dev.goodwy.rphone.view.screen.settings.KEY_SELECTED_APP_ICON
import dev.goodwy.rphone.view.screen.settings.applyIcon
import dev.goodwy.rphone.view.screen.settings.buildIcons
import io.github.bootika.dotdialer.core.diagnostics.DiagnosticEvent
import io.github.bootika.dotdialer.diagnostics.AppDiagnostics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RillApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDiagnostics.initialize(this)
        AppDiagnostics.record(
            DiagnosticEvent.AppStarted(
                versionName = BuildConfig.VERSION_NAME,
                versionCode = BuildConfig.VERSION_CODE.toLong(),
                flavor = BuildConfig.FLAVOR,
                apiLevel = Build.VERSION.SDK_INT,
                manufacturer = Build.MANUFACTURER,
                model = Build.MODEL,
            )
        )
        startKoin {
            androidContext(this@RillApp)
            modules(appModule)
        }
        restoreSavedAppIcon()
    }

    private fun restoreSavedAppIcon() {
        try {
            val prefs = getSharedPreferences("rill_prefs", MODE_PRIVATE)
            val savedKey = prefs.getString(KEY_SELECTED_APP_ICON, "default") ?: "default"
            val icons = buildIcons(this)
            val entry = icons.find { it.key == savedKey } ?: icons.first()
            applyIcon(this, icons, entry)
        } catch (_: Exception) {}
    }
}
