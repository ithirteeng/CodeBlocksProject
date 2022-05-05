package com.example.codeblocksproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ConsoleFragment : Fragment(R.layout.fragment_console) {


    var isClosedStart = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeSlidingFragment(view)

    }

    private fun closeSlidingFragment(view: View) {
        val button = view.findViewById<Button>(R.id.closeButton)
        button.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(0, R.anim.bottom_panel_slide_in)
            this.onCreateAnimation(0, true, 1)
            transaction.hide(this)
            transaction.commit()
            isClosedStart = true
            Handler(Looper.getMainLooper()).postDelayed({
                kotlin.run {
                    (parentFragment as MainFragment).displayButtons()
                }
            }, 350)

        }
    }

    fun changeTheme(color: String, context: Context) {
        val closeButton = view?.findViewById<Button>(R.id.closeButton)
        closeButton?.setTextColor(getColor(R.color.chocolateMainColor, context))
        when (color) {
            MainFragment.SPACE_COLOR -> {
                val textView = view?.findViewById<TextView>(R.id.textFieldView)
                textView?.setBackgroundColor(getColor(R.color.spaceConsoleColor, context))
                textView?.setTextColor(getColor(R.color.chocolateMainColor, context))
                val imageView = view?.findViewById<ImageView>(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.spaceBottomButtonsColor, context))
            }
            MainFragment.PINK_COLOR -> {
                val textView = view?.findViewById<TextView>(R.id.textFieldView)
                textView?.setBackgroundColor(getColor(R.color.pinkConsoleColor, context))
                textView?.setTextColor(getColor(R.color.chocolateMainColor, context))
                val imageView = view?.findViewById<ImageView>(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.pinkBottomButtonsColor, context))
            }
            MainFragment.CHOCOLATE_COLOR -> {
                val textView = view?.findViewById<TextView>(R.id.textFieldView)
                textView?.setBackgroundColor(getColor(R.color.chocolateConsoleColor, context))
                textView?.setTextColor(getColor(R.color.black, context))
                val imageView = view?.findViewById<ImageView>(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.chocolateBottomButtonsColor, context))
            }
            MainFragment.SEPIA_COLOR -> {
                val textView = view?.findViewById<TextView>(R.id.textFieldView)
                textView?.setBackgroundColor(getColor(R.color.sepiaConsoleColor, context))
                textView?.setTextColor(getColor(R.color.white, context))
                val imageView = view?.findViewById<ImageView>(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.sepiaBottomButtonsColor, context))
                closeButton?.setTextColor(getColor(R.color.white, context))
            }
            MainFragment.SHREK_COLOR -> {
                val textView = view?.findViewById<TextView>(R.id.textFieldView)
                textView?.setBackgroundColor(getColor(R.color.shrekConsoleColor, context))
                textView?.setTextColor(getColor(R.color.black, context))
                val imageView = view?.findViewById<ImageView>(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.shrekBottomButtonsColor, context))
            }

        }
    }

    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }

}