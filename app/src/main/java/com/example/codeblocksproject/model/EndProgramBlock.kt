package com.example.codeblocksproject.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

@SuppressLint("ViewConstructor")
class EndProgramBlock @JvmOverloads constructor(
    view: View,
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {
    override val isNestingPossible = false
    override var previousId: Int = 0
    override var nextId: Int = -1
    override val blockType = BlockTypes.END_PROGRAM_BLOCK_TYPE
    override val blockView: View = view
    //TODO: change to end program layout

    override var position = 0
    override val pattern = "}"

    override fun blockToCode(): String {
        return pattern
    }
    override fun makeEditTextsDisabled() {
        TODO("Not yet implemented")
    }
}