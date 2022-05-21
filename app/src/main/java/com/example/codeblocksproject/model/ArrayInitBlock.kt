package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.codeblocksproject.R
import com.example.codeblocksproject.databinding.BlockArrayInitBinding

class ArrayInitBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, LinearLayout(context, attrs) {
    private val binding = BlockArrayInitBinding.inflate(LayoutInflater.from(context), this)

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = binding.root
    override val blockType = BlockTypes.ARRAY_INIT_BLOCK_TYPE
    override val pattern = "var <name> : Array(<length>);"
    override var position = 0

    override fun blockToCode() = pattern.replace("<name>", binding.varName.text.toString())
        .replace("<length>", binding.varLength.text.toString())

    override fun makeEditTextsDisabled() {
        convertEditTextToTextView(binding.lengthText, binding.varLength)
        convertEditTextToTextView(binding.nameText, binding.varName)
    }

    override fun ifTextViewEmpty() =
        binding.lengthText.text.isEmpty() || binding.nameText.text.isEmpty()


    override fun content() =
        arrayListOf(binding.nameText.text.toString(), binding.lengthText.text.toString())


    override fun loadBlock(data: BlockData) {
        this.id = data.id
        this.nextId = data.nextId
        this.previousId = data.prevId
        this.position = data.position

        binding.nameText.text = data.content[0]
        binding.varName.setText(data.content[0])

        binding.lengthText.text = data.content[1]
        binding.varLength.setText(data.content[1])
    }

    init {
        toEditText(binding.lengthText, binding.varLength, context)
        toEditText(binding.nameText, binding.varName, context)
        toTextView(binding.varLength, binding.lengthText)
        toTextView(binding.varName, binding.nameText)

        blockView.setBackgroundResource(R.drawable.block_array_init_background)
        blockView.setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.startAndEndBlockPadding),
            context.resources.getDimensionPixelOffset(R.dimen.topAndBottomBlockPadding)
        )
    }
}