package com.example.codeblocksproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class BlocksFragment : Fragment(R.layout.fragment_blocks) {
    var isClosedBlocks = true
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
            isClosedBlocks = true
            Handler(Looper.getMainLooper()).postDelayed({
                kotlin.run {
                    (parentFragment as MainFragment).displayButtons()
                }
            }, 350)
        }
    }

    fun changeTheme(color: String, context: Context) {
        when (color) {
            MainFragment.SPACE_COLOR -> {
                var imageView = view?.findViewById<ImageView>(R.id.blocksBackground)
                imageView?.setBackgroundColor(getColor(R.color.spaceTextFieldColor, context))
                imageView = view?.findViewById(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.spaceBottomButtonsColor, context))
            }
            MainFragment.PINK_COLOR -> {
                var imageView = view?.findViewById<ImageView>(R.id.blocksBackground)
                imageView?.setBackgroundColor(getColor(R.color.pinkTextFieldColor, context))
                imageView = view?.findViewById(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.pinkBottomButtonsColor, context))
            }
            MainFragment.CHOCOLATE_COLOR -> {
                var imageView = view?.findViewById<ImageView>(R.id.blocksBackground)
                imageView?.setBackgroundColor(getColor(R.color.chocolateTextFieldColor, context))
                imageView = view?.findViewById(R.id.buttonBackground)
                imageView?.setBackgroundColor(getColor(R.color.chocolateBottomButtonsColor, context))
            }

        }
    }

    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }


}