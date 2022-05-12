package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocksproject.R

class AssignmentBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View =
        LayoutInflater.from(context).inflate(R.layout.initialization_block, this)
    //TODO: Change view to assign

    override val blockType = BlockTypes.ASSIGN_BLOCK_TYPE
    override val pattern = "<name> = <value>;"
    override var position = 0
    override fun blockToCode(): String {
        val varName = blockView.findViewById<EditText>(R.id.varName).text.toString()
        val varValue = blockView.findViewById<EditText>(R.id.varValue).text.toString()

        return pattern.replace("<name>", varName).replace("<value>", varValue)
    }

    override fun makeEditTextsDisabled() {
        TODO("Not yet implemented")
    }


}