package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.codeblocksproject.R
import com.example.codeblocksproject.databinding.BlockWhileBinding

class WhileBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    private val binding = BlockWhileBinding.inflate(LayoutInflater.from(context), this)

    override val isNestingPossible = true
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = binding.root
    override val blockType = BlockTypes.WHILE_BLOCK_TYPE
    override val pattern = "while(<condition>)"
    override var position = 0

    override fun blockToCode() =
        pattern.replace("<condition>", binding.conditionText.text.toString())


    override fun makeEditTextsDisabled() {
        convertEditTextToTextView(binding.conditionText, binding.condition)
    }

    override fun ifTextViewEmpty() = binding.conditionText.text.isEmpty()


    override fun content() = arrayListOf(binding.conditionText.text.toString())


    override fun loadBlock(data: BlockData) {
        this.id = data.id
        this.nextId = data.nextId
        this.previousId = data.prevId
        this.position = data.position

        binding.conditionText.text = data.content[0]
        binding.condition.setText(data.content[0])
    }

    init {
        toTextView(binding.condition, binding.conditionText)
        toEditText(binding.conditionText, binding.condition, context)

        blockView.setBackgroundResource(R.drawable.while_block_background)
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}