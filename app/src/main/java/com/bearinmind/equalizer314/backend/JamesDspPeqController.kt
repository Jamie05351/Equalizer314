package com.bearinmind.equalizer314.backend

import android.content.Context
import android.content.Intent
import android.util.Log
import com.bearinmind.equalizer314.dsp.BiquadFilter
import com.bearinmind.equalizer314.dsp.ParametricEqualizer

class JamesDspPeqController(
    private val context: Context
) {

    companion object {
        private const val TAG = "JamesDspPeqController"

        private const val ROOTLESS_JAMESDSP_PACKAGE = "me.timschneeberger.rootlessjamesdsp"
        private const val ACTION_SET_PEQ = "me.timschneeberger.rootlessjamesdsp.SET_PEQ"

        private const val EXTRA_PEQ_ENABLED = "rootlessjamesdsp.peq.enabled"
        private const val EXTRA_PEQ_COUNT = "rootlessjamesdsp.peq.count"
        private const val EXTRA_PREFIX = "rootlessjamesdsp.peq"

        private const val MAX_FILTERS = 6

        private const val TYPE_PEAK = 0
        private const val TYPE_LOW_SHELF = 1
        private const val TYPE_HIGH_SHELF = 2
        private const val TYPE_LOW_PASS = 3
        private const val TYPE_HIGH_PASS = 4
        private const val TYPE_ALL_PASS = 5
        private const val TYPE_NOTCH = 6
        private const val TYPE_BAND_PASS = 7
    }

    fun sendPeq(eq: ParametricEqualizer?) {
        val safeEq = eq
        val bands = safeEq
            ?.getAllBands()
            ?.filter { it.enabled }
            ?.take(MAX_FILTERS)
            .orEmpty()

        val intent = Intent(ACTION_SET_PEQ).apply {
            setPackage(ROOTLESS_JAMESDSP_PACKAGE)
            putExtra(EXTRA_PEQ_ENABLED, safeEq?.isEnabled == true && bands.isNotEmpty())
            putExtra(EXTRA_PEQ_COUNT, bands.size)

            bands.forEachIndexed { index, band ->
                val slot = index + 1
                putExtra(extraType(slot), mapFilterType(band.filterType))
                putExtra(extraFreq(slot), band.frequency.coerceIn(1f, 20000f))
                putExtra(extraGain(slot), band.gain.coerceIn(-24f, 24f))
                putExtra(extraQ(slot), band.q.toFloat().coerceAtLeast(0.01f))
            }
        }

        try {
            context.sendBroadcast(intent)
            Log.d(TAG, "Sent Rootless JamesDSP PEQ update bands=${bands.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send Rootless JamesDSP PEQ update", e)
        }
    }

    private fun mapFilterType(type: BiquadFilter.FilterType): Int = when (type) {
        BiquadFilter.FilterType.BELL -> TYPE_PEAK
        BiquadFilter.FilterType.LOW_SHELF -> TYPE_LOW_SHELF
        BiquadFilter.FilterType.HIGH_SHELF -> TYPE_HIGH_SHELF
        BiquadFilter.FilterType.LOW_PASS -> TYPE_LOW_PASS
        BiquadFilter.FilterType.HIGH_PASS -> TYPE_HIGH_PASS
        BiquadFilter.FilterType.LOW_SHELF_1 -> TYPE_LOW_SHELF
        BiquadFilter.FilterType.HIGH_SHELF_1 -> TYPE_HIGH_SHELF
        BiquadFilter.FilterType.LOW_PASS_1 -> TYPE_LOW_PASS
        BiquadFilter.FilterType.HIGH_PASS_1 -> TYPE_HIGH_PASS
        BiquadFilter.FilterType.BAND_PASS -> TYPE_BAND_PASS
        BiquadFilter.FilterType.NOTCH -> TYPE_NOTCH
        BiquadFilter.FilterType.ALL_PASS -> TYPE_ALL_PASS
    }

    private fun extraType(slot: Int) = "$EXTRA_PREFIX.$slot.type"
    private fun extraFreq(slot: Int) = "$EXTRA_PREFIX.$slot.freq"
    private fun extraGain(slot: Int) = "$EXTRA_PREFIX.$slot.gain"
    private fun extraQ(slot: Int) = "$EXTRA_PREFIX.$slot.q"
}
