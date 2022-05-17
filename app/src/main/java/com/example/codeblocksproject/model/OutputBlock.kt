package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocksproject.R

class OutputBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    private val view =
        LayoutInflater.from(context).inflate(R.layout.output_block, this).apply {
            val conditionTextView = findViewById<TextView>(R.id.expressionText)
            val conditionEditText = findViewById<EditText>(R.id.expression)

            toTextView(conditionEditText, conditionTextView)
            toEditText(conditionTextView, conditionEditText, context)

        }

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = view
    override val blockType = BlockTypes.OUTPUT_BLOCK_TYPE
    override val pattern = "print(<expression>);"
    override var position = 0

    override fun blockToCode(): String {
        val expression = view.findViewById<TextView>(R.id.expression).text.toString()
        return pattern.replace("<expression>", expression)
    }

    override fun makeEditTextsDisabled() {
        val conditionTextView = findViewById<TextView>(R.id.expressionText)
        val conditionEditText = findViewById<EditText>(R.id.expression)
        convertEditTextToTextView(conditionTextView, conditionEditText)
    }

    override fun ifTextViewEmpty(): Boolean {
        val conditionTextView = findViewById<TextView>(R.id.expressionText)
        return conditionTextView.text.isEmpty()
    }

    init {
        blockView.setBackgroundResource(R.drawable.output_block_background)
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}