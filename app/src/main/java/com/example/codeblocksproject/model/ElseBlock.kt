package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.codeblocksproject.R

class ElseBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {

    override val isNestingPossible = true
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View =
        LayoutInflater.from(context).inflate(R.layout.else_block, this)
    override val blockType = BlockTypes.ELSE_BLOCK_TYPE
    override val pattern = "else"
    override var position = 0
    override fun blockToCode(): String {
        return pattern
    }

    override fun makeEditTextsDisabled() {
    }

    override fun ifTextViewEmpty(): Boolean {
        return false
    }

    init {
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}