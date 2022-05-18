package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.codeblocksproject.R

class WhileBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    private val view =
        LayoutInflater.from(context).inflate(R.layout.while_block, this).apply {
            val conditionTextView = findViewById<TextView>(R.id.conditionText)
            val conditionEditText = findViewById<EditText>(R.id.condition)

            toTextView(conditionEditText, conditionTextView)
            toEditText(conditionTextView, conditionEditText, context)

        }

    override val isNestingPossible = true
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = view
    override val blockType = BlockTypes.WHILE_BLOCK_TYPE
    override val pattern = "while(<condition>)"
    override var position = 0

    override fun blockToCode(): String {
        val condition = view.findViewById<TextView>(R.id.conditionText).text.toString()
        return pattern.replace("<condition>", condition)
    }

    override fun makeEditTextsDisabled() {
        val conditionTextView = findViewById<TextView>(R.id.conditionText)
        val conditionEditText = findViewById<EditText>(R.id.condition)
        convertEditTextToTextView(conditionTextView, conditionEditText)
    }

    override fun ifTextViewEmpty(): Boolean {
        val conditionTextView = findViewById<TextView>(R.id.conditionText)
        return conditionTextView.text.isEmpty()
    }

    init {
        blockView.setBackgroundResource(R.drawable.while_block_background)
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}