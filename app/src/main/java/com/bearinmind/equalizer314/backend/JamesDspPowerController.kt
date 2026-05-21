package com.bearinmind.equalizer314.backend

import android.content.Context
import android.content.Intent
import android.util.Log

class JamesDspPowerController(
    private val context: Context
) {

    companion object {
        private const val TAG = "JamesDspPowerController"
        private const val ROOTLESS_JAMESDSP_PACKAGE = "me.timschneeberger.rootlessjamesdsp"
        private const val ACTION_SET_POWER_STATE = "me.timschneeberger.rootlessjamesdsp.SET_POWER_STATE"
        private const val EXTRA_ENABLED = "rootlessjamesdsp.enabled"
    }

    fun setPowered(enabled: Boolean) {
        val intent = Intent(ACTION_SET_POWER_STATE).apply {
            setPackage(ROOTLESS_JAMESDSP_PACKAGE)
            putExtra(EXTRA_ENABLED, enabled)
        }

        try {
            context.sendBroadcast(intent)
            Log.d(TAG, "Sent Rootless JamesDSP power broadcast enabled=$enabled")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send Rootless JamesDSP power broadcast", e)
        }
    }
}
