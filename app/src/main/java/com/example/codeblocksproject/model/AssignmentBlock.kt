package com.example.codeblocksproject.model

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.codeblocksproject.R

class AssignmentBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomView, ConstraintLayout(context, attrs) {
    private val view =
        LayoutInflater.from(context).inflate(R.layout.assignment_block, this).apply {
            val valueEdit: EditText = findViewById(R.id.varValue)
            val nameEdit: EditText = findViewById(R.id.varName)
            val valueText: TextView = findViewById(R.id.valueText)
            val nameText: TextView = findViewById(R.id.nameText)

            toEditText(valueText, valueEdit, context, nameText, nameEdit)
            toEditText(nameText, nameEdit, context, valueText, valueEdit)
            toTextView(valueEdit, valueText)
            toTextView(nameEdit, nameText)

        }

    override val isNestingPossible = false
    override var previousId: Int = -1
    override var nextId: Int = -1
    override val blockView: View = view
    override val blockType = BlockTypes.INPUT_BLOCK_TYPE
    override val pattern = "<name> = <value>;"
    override var position = 0

    override fun blockToCode(): String {
        val varName = blockView.findViewById<EditText>(R.id.varName).text.toString()
        val varValue = blockView.findViewById<EditText>(R.id.varValue).text.toString()

        return pattern.replace("<name>", varName).replace("<value>", varValue)
    }

    override fun makeEditTextsDisabled() {
        val valueEdit: EditText = view.findViewById(R.id.varValue)
        val nameEdit: EditText = view.findViewById(R.id.varName)
        val valueText: TextView = view.findViewById(R.id.valueText)
        val nameText: TextView = view.findViewById(R.id.nameText)
        convertEditTextToTextView(valueText, valueEdit)
        convertEditTextToTextView(nameText, nameEdit)
    }

    private fun toEditText(
        textView: TextView,
        editText: EditText,
        context: Context,
        otherTextView: TextView,
        otherText: EditText
    ) {
        textView.setOnClickListener {
            editText.visibility = View.VISIBLE
            textView.visibility = View.INVISIBLE

            editText.requestFocus()
            editText.isFocusableInTouchMode = true
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
            convertEditTextToTextView(otherTextView, otherText)
        }
    }
}