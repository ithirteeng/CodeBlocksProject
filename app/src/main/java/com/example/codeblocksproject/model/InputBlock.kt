package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.codeblocksproject.R
import com.example.codeblocksproject.databinding.BlockInputBinding

class InputBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    private val binding = BlockInputBinding.inflate(LayoutInflater.from(context), this)

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = binding.root
    override val blockType = BlockTypes.INPUT_BLOCK_TYPE
    override val pattern = "input(<expression>);"
    override var position = 0

    override fun blockToCode() = pattern.replace("<expression>", binding.expression.text.toString())


    override fun makeEditTextsDisabled() {
        convertEditTextToTextView(binding.expressionText, binding.expression)
    }

    override fun ifTextViewEmpty() = binding.expressionText.text.isEmpty()


    override fun content() = arrayListOf(binding.expression.text.toString())


    override fun loadBlock(data: BlockData) {
        this.id = data.id
        this.nextId = data.nextId
        this.previousId = data.prevId
        this.position = data.position

        binding.expressionText.text = data.content[0]
        binding.expression.setText(data.content[0])
    }

    init {
        toTextView(binding.expression, binding.expressionText)
        toEditText(binding.expressionText, binding.expression, context)

        blockView.setBackgroundResource(R.drawable.block_output_background)
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}