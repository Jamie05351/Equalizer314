package com.bearinmind.equalizer314.backend

import com.bearinmind.equalizer314.audio.DynamicsProcessingManager
import com.bearinmind.equalizer314.dsp.ParametricEqualizer

class DynamicsProcessingBackend(
    private val manager: DynamicsProcessingManager
) : DspBackend {

    override val isActive: Boolean
        get() = manager.isActive

    override fun start(eq: ParametricEqualizer) {
        manager.start(eq)
    }

    override fun updateFromEqualizer(eq: ParametricEqualizer) {
        manager.updateFromEqualizer(eq)
    }

    override fun updateFromEqualizers(
        leftEq: ParametricEqualizer,
        rightEq: ParametricEqualizer
    ) {
        manager.updateFromEqualizers(leftEq, rightEq)
    }

    override fun updateChannelSettings() {
        manager.updateChannelSettings()
    }

    override fun updateLimiter() {
        manager.updateLimiter()
    }

    override fun pushLimiterUpdate() {
        manager.pushLimiterUpdate()
    }

    override fun applyMbcBands(
        bands: List<DynamicsProcessingManager.MbcBandParams>,
        crossovers: FloatArray
    ) {
        manager.applyMbcBands(bands, crossovers)
    }

    override fun setEnabled(enabled: Boolean) {
        manager.setEnabled(enabled)
    }

    override fun stop() {
        manager.stop()
    }
}
