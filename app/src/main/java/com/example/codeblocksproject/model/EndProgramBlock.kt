package com.example.codeblocksproject

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocksproject.model.CustomView

const val END_PROGRAM_BLOCK_TYPE = "programend"
const val END_PROGRAM_BLOCK_ID = 1

class EndProgramBlock @JvmOverloads constructor(
    view: View,
    context: Context, attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {
    override val isNestingPossible = false
    override var previousId: Int = START_PROGRAM_BLOCK_ID
    override var nextId: Int = -1
    override val blockType = END_PROGRAM_BLOCK_TYPE
    override val blockView: View = view
    //TODO(change to end program layout)

    override val pattern = ""

    override fun blockToCode(): String {
        return ""
    }
}