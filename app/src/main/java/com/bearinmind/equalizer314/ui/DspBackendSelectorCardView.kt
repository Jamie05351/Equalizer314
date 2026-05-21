package com.bearinmind.equalizer314.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bearinmind.equalizer314.R
import com.bearinmind.equalizer314.backend.DspBackendPreferences
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class DspBackendSelectorCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val backendPrefs = DspBackendPreferences(context.applicationContext)
    private var lastDismissAt = 0L

    override fun onFinishInflate() {
        super.onFinishInflate()
        wireDropdown()
    }

    private fun wireDropdown() {
        val layout = findViewById<TextInputLayout>(R.id.dspBackendDropdownLayout) ?: return
        val dropdown = findViewById<MaterialAutoCompleteTextView>(R.id.dspBackendDropdown) ?: return

        val entries = listOf(LABEL_DYNAMICS_PROCESSING, LABEL_ROOTLESS_JAMESDSP)
        dropdown.setAdapter(
            ArrayAdapter(context, android.R.layout.simple_list_item_1, entries)
        )
        dropdown.setText(labelForMode(backendPrefs.getBackendMode()), false)

        dropdown.setOnDismissListener {
            lastDismissAt = System.currentTimeMillis()
        }

        layout.setOnClickListener {
            if (System.currentTimeMillis() - lastDismissAt < 300) {
                lastDismissAt = 0L
                return@setOnClickListener
            }
            if (dropdown.isPopupShowing) {
                dropdown.dismissDropDown()
            } else {
                dropdown.showDropDown()
            }
        }

        dropdown.setOnItemClickListener { _, _, position, _ ->
            val mode = when (entries.getOrNull(position)) {
                LABEL_ROOTLESS_JAMESDSP -> DspBackendPreferences.BACKEND_ROOTLESS_JAMESDSP
                else -> DspBackendPreferences.BACKEND_DYNAMICS_PROCESSING
            }
            backendPrefs.saveBackendMode(mode)
            dropdown.setText(labelForMode(mode), false)
            dropdown.clearFocus()
            Toast.makeText(
                context,
                "DSP backend saved. Restart EQ to apply.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun labelForMode(mode: Int): String = when (mode) {
        DspBackendPreferences.BACKEND_ROOTLESS_JAMESDSP -> LABEL_ROOTLESS_JAMESDSP
        else -> LABEL_DYNAMICS_PROCESSING
    }

    companion object {
        private const val LABEL_DYNAMICS_PROCESSING = "Android DynamicsProcessing"
        private const val LABEL_ROOTLESS_JAMESDSP = "Rootless JamesDSP"
    }
}
