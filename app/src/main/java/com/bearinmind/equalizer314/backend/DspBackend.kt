package com.bearinmind.equalizer314.backend

import com.bearinmind.equalizer314.audio.DynamicsProcessingManager
import com.bearinmind.equalizer314.dsp.ParametricEqualizer

interface DspBackend {
    val isActive: Boolean

    fun start(eq: ParametricEqualizer)

    fun updateFromEqualizer(eq: ParametricEqualizer)

    fun updateFromEqualizers(
        leftEq: ParametricEqualizer,
        rightEq: ParametricEqualizer
    )

    fun updateChannelSettings()

    fun updateLimiter()

    fun pushLimiterUpdate()

    fun applyMbcBands(
        bands: List<DynamicsProcessingManager.MbcBandParams>,
        crossovers: FloatArray
    )

    fun setEnabled(enabled: Boolean)

    fun stop()
}
