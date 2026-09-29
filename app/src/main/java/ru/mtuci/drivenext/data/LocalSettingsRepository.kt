package ru.mtuci.drivenext.data

import android.content.Context

interface LocalSettingsRepository {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted()
    fun hasAccessToken(): Boolean
}

class SharedPreferencesSettingsRepository(context: Context) : LocalSettingsRepository {
    private val preferences = context.applicationContext.getSharedPreferences("drivenext", Context.MODE_PRIVATE)

    override fun isOnboardingCompleted(): Boolean =
        preferences.getBoolean("onboarding_completed", false)

    override fun setOnboardingCompleted() {
        preferences.edit().putBoolean("onboarding_completed", true).apply()
    }

    override fun hasAccessToken(): Boolean =
        preferences.getString("access_token", null).orEmpty().isNotBlank()
}
