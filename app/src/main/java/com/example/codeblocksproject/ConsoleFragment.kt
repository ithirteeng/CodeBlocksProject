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
                changeColor(
                    R.color.spaceConsoleColor,
                    R.color.chocolateMainColor,
                    R.color.spaceBottomButtonsColor,
                    context
                )
            }
            MainFragment.PINK_COLOR -> {
                changeColor(
                    R.color.pinkConsoleColor,
                    R.color.chocolateMainColor,
                    R.color.pinkBottomButtonsColor,
                    context
                )
            }
            MainFragment.CHOCOLATE_COLOR -> {
                changeColor(
                    R.color.chocolateConsoleColor,
                    R.color.black,
                    R.color.chocolateBottomButtonsColor,
                    context
                )
            }
            MainFragment.MONOCHROME_COLOR -> {
                changeColor(
                    R.color.monochromeConsoleColor,
                    R.color.white,
                    R.color.monochromeBottomButtonsColor,
                    context
                )
                closeButton?.setTextColor(getColor(R.color.white, context))
            }
            MainFragment.SHREK_COLOR -> {
                changeColor(
                    R.color.shrekConsoleColor,
                    R.color.black,
                    R.color.shrekBottomButtonsColor,
                    context
                )
            }

        }
    }

    private fun changeColor(backgroundColor: Int, textColor: Int, buttonsColor: Int, context: Context) {
        val textView = view?.findViewById<TextView>(R.id.textFieldView)
        textView?.setBackgroundColor(getColor(backgroundColor, context))
        textView?.setTextColor(getColor(textColor, context))

        val imageView = view?.findViewById<ImageView>(R.id.buttonBackground)
        imageView?.setBackgroundColor(getColor(buttonsColor, context))
    }

    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }

}