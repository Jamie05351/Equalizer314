package com.bearinmind.equalizer314.backend

import android.content.Context

class DspBackendPreferences(context: Context) {

    companion object {
        const val BACKEND_DYNAMICS_PROCESSING = 0
        const val BACKEND_ROOTLESS_JAMESDSP = 1

        private const val PREFS_NAME = "dsp_backend_settings"
        private const val KEY_BACKEND_MODE = "backend_mode"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getBackendMode(): Int = prefs.getInt(KEY_BACKEND_MODE, BACKEND_DYNAMICS_PROCESSING)

    fun saveBackendMode(mode: Int) {
        val safeMode = when (mode) {
            BACKEND_ROOTLESS_JAMESDSP -> BACKEND_ROOTLESS_JAMESDSP
            else -> BACKEND_DYNAMICS_PROCESSING
        }
        prefs.edit().putInt(KEY_BACKEND_MODE, safeMode).apply()
    }

    fun isRootlessJamesDspSelected(): Boolean = getBackendMode() == BACKEND_ROOTLESS_JAMESDSP
}
