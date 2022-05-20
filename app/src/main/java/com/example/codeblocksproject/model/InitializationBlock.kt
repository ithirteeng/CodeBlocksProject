package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.codeblocksproject.R
import com.example.codeblocksproject.databinding.BlockInitializationBinding

class InitializationBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    private val binding = BlockInitializationBinding.inflate(LayoutInflater.from(context), this)

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = binding.root
    override val blockType = BlockTypes.INIT_BLOCK_TYPE
    override val pattern = "var <name> : Int = <value>;"
    override var position = 0

    override fun blockToCode() = pattern.replace("<name>", binding.varName.text.toString())
        .replace("<value>", binding.varValue.text.toString())


    override fun makeEditTextsDisabled() {
        convertEditTextToTextView(binding.valueText, binding.varValue)
        convertEditTextToTextView(binding.nameText, binding.varName)
    }

    override fun ifTextViewEmpty() =
        binding.valueText.text.isEmpty() || binding.nameText.text.isEmpty()


    override fun content() =
        arrayListOf(binding.nameText.text.toString(), binding.valueText.text.toString())

    override fun loadBlock(data: BlockData) {
        this.id=data.id
        this.nextId=data.nextId
        this.previousId=data.prevId
        this.position=data.position

        binding.nameText.text=data.content[0]
        binding.varName.setText(data.content[0])

        binding.valueText.text=data.content[1]
        binding.varValue.setText(data.content[1])
    }

    init {
        toEditText(binding.valueText, binding.varValue, context)
        toEditText(binding.nameText, binding.varName, context)
        toTextView(binding.varValue, binding.valueText)
        toTextView(binding.varName, binding.nameText)

        blockView.setBackgroundResource(R.drawable.init_block_background)
        blockView.setBackgroundResource(R.drawable.block_init_background)
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}


