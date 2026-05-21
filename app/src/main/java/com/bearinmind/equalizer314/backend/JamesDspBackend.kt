package com.bearinmind.equalizer314.backend

import android.util.Log
import com.bearinmind.equalizer314.audio.DynamicsProcessingManager
import com.bearinmind.equalizer314.dsp.ParametricEqualizer

class JamesDspBackend : DspBackend {

    companion object {
        private const val TAG = "JamesDspBackend"
    }

    private var active = false
    private var lastLeftEq: ParametricEqualizer? = null
    private var lastRightEq: ParametricEqualizer? = null

    override val isActive: Boolean
        get() = active

    override fun start(eq: ParametricEqualizer) {
        lastLeftEq = eq
        lastRightEq = eq
        active = true
        Log.d(TAG, "start")
        applyFullState()
    }

    override fun updateFromEqualizer(eq: ParametricEqualizer) {
        updateFromEqualizers(eq, eq)
    }

    override fun updateFromEqualizers(
        leftEq: ParametricEqualizer,
        rightEq: ParametricEqualizer
    ) {
        lastLeftEq = leftEq
        lastRightEq = rightEq
        Log.d(TAG, "updateFromEqualizers")
        applyEqState()
    }

    override fun updateChannelSettings() {
        Log.d(TAG, "updateChannelSettings")
        applyFullState()
    }

    override fun updateLimiter() {
        Log.d(TAG, "updateLimiter")
        applyLimiterState()
    }

    override fun pushLimiterUpdate() {
        Log.d(TAG, "pushLimiterUpdate")
        applyLimiterState()
    }

    override fun applyMbcBands(
        bands: List<DynamicsProcessingManager.MbcBandParams>,
        crossovers: FloatArray
    ) {
        Log.d(TAG, "applyMbcBands bands=${bands.size} crossovers=${crossovers.size}")
        applyMbcState()
    }

    override fun setEnabled(enabled: Boolean) {
        active = enabled
        Log.d(TAG, "setEnabled=$enabled")
    }

    override fun stop() {
        active = false
        lastLeftEq = null
        lastRightEq = null
        Log.d(TAG, "stop")
    }

    private fun applyEqState() {
        // TODO: Render Equalizer314 ParametricEqualizer to JamesDSP-compatible params.
        // TODO: Write via Shizuku bridge.
        // TODO: Trigger Rootless JamesDSP reload/apply.
    }

    private fun applyLimiterState() {
        // TODO: Render limiter settings to JamesDSP-compatible params.
    }

    private fun applyMbcState() {
        // TODO: Render multiband compressor settings to JamesDSP-compatible params.
    }

    private fun applyFullState() {
        applyEqState()
        applyLimiterState()
        applyMbcState()
    }
}
