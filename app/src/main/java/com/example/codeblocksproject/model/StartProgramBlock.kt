package com.example.codeblocksproject.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

@SuppressLint("ViewConstructor")
class StartProgramBlock @JvmOverloads constructor(
    view: View,
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {
    override val isNestingPossible = false
    override var previousId = -1
    override var nextId = 0
    override val blockType = BlockTypes.START_PROGRAM_BLOCK_TYPE
    override val blockView = view

    override val pattern = "{"
    override var position=0

    override fun blockToCode(): String {
        return pattern
    }
}