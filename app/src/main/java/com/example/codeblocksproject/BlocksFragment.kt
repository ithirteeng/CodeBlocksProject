package com.example.codeblocksproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class BlocksFragment : Fragment(R.layout.fragment_blocks) {
    var isClosedBlocks = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeSlidingFragment(view)
        hui(view)
    }

    private fun hui(view: View){
        val button=view.findViewById<ImageButton>(R.id.initializationBlock)
        button.setOnClickListener {
            (parentFragment as MainFragment).addBlock()
        }
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
            isClosedBlocks = true
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
                    R.color.spaceBottomButtonsColor,
                    context
                )
            }
            MainFragment.PINK_COLOR -> {
                changeColor(
                    R.color.pinkConsoleColor,
                    R.color.pinkBottomButtonsColor,
                    context
                )
            }
            MainFragment.CHOCOLATE_COLOR -> {
                changeColor(
                    R.color.chocolateConsoleColor,
                    R.color.chocolateBottomButtonsColor,
                    context
                )
            }
            MainFragment.MONOCHROME_COLOR -> {
                changeColor(
                    R.color.monochromeConsoleColor,
                    R.color.monochromeBottomButtonsColor,
                    context
                )
                closeButton?.setTextColor(getColor(R.color.white, context))
            }
            MainFragment.SHREK_COLOR -> {
                changeColor(
                    R.color.shrekConsoleColor,
                    R.color.shrekBottomButtonsColor,
                    context
                )
            }
        }
    }

    private fun changeColor(backgroundColor: Int, buttonColor: Int, context: Context) {
        var imageView = view?.findViewById<ImageView>(R.id.blocksBackground)
        imageView?.setBackgroundColor(getColor(backgroundColor, context))

        imageView = view?.findViewById(R.id.buttonBackground)
        imageView?.setBackgroundColor(getColor(buttonColor, context))
    }

    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }


}