package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocksproject.R

class OutputBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View =
        LayoutInflater.from(context).inflate(R.layout.initialization_block, this)
    //TODO:Change to output block

    override val blockType = BlockTypes.OUTPUT_BLOCK_TYPE
    override val pattern = ""

    //TODO:Change to output pattern
    override var position = 0
    override fun blockToCode(): String {
        return pattern
    }
}