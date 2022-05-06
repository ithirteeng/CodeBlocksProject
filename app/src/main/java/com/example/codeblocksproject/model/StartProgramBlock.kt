package com.example.codeblocksproject

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.CustomView

const val START_PROGRAM_BLOCK_TYPE = "programStart"
const val START_PROGRAM_BLOCK_ID = 0

class StartProgramBlock @JvmOverloads constructor(
    view: View,
    context: Context, attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {
    override val isNestingPossible = false
    override var previousId = -1
    override var nextId = END_PROGRAM_BLOCK_ID
    override val blockType = START_PROGRAM_BLOCK_TYPE
    override val blockView = view

    override val pattern = ""

    override fun blockToCode(): String {
        return ""
    }
}