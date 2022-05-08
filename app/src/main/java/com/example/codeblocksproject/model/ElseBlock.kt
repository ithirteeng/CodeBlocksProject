package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocksproject.R

class ElseBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {

    override val isNestingPossible = true
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = LayoutInflater.from(context).inflate(R.layout.initialization_block, this)
    //TODO: Change to else block

    override val blockType = BlockTypes.ELSE_BLOCK_TYPE
    override val pattern = ""
    //TODO: Change to else pattern
    override var position=0
    override fun blockToCode(): String {
        return ""
    }
}