package com.example.codeblocksproject.model

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

interface CustomView {

    val isNestingPossible: Boolean
    var previousId: Int
    var nextId: Int
    val blockType: String
    val blockView: View
    val pattern: String
    var position: Int

    fun toEditText(textView: TextView, editText: EditText, context: Context) {
        textView.setOnClickListener {
            editText.visibility = View.VISIBLE
            textView.visibility = View.GONE

            editText.requestFocus()
            editText.isFocusableInTouchMode = true
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        }
    }

    fun toTextView(editText: EditText, textView: TextView) {
        editText.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                textView.text = editText.text
                editText.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        }
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event != null &&
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (event == null || !event.isShiftPressed) {
                    textView.text = editText.text
                    editText.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                }
            }
            false
        }
    }

    fun blockToCode(): String

    fun convertEditTextToTextView(textView: TextView, editText: EditText) {
        textView.text = editText.text
        editText.visibility = View.GONE
        textView.visibility = View.VISIBLE
    }

    fun makeEditTextsDisabled()

    fun ifTextViewEmpty() : Boolean

}