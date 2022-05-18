package com.example.codeblocksproject.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.codeblocksproject.R

@SuppressLint("ViewConstructor")
class StartProgramBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    override val isNestingPossible = false
    override var previousId = -1
    override var nextId = 0
    override val blockType = BlockTypes.START_PROGRAM_BLOCK_TYPE
    override val blockView: View =
        LayoutInflater.from(context).inflate(R.layout.start_program_block, this)

    override val pattern = "{"
    override var position = 0

    override fun blockToCode(): String {
        return pattern
    }

    override fun makeEditTextsDisabled() {
    }

    override fun ifTextViewEmpty(): Boolean {
        return false
    }
}