package com.example.codeblocksproject

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class StartFragment : Fragment(R.layout.fragment_textfield) {
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
            Handler().postDelayed(Runnable {
                kotlin.run {
                    (parentFragment as MainFragment).displayButtons()
                }
            }, 650)

        }
    }

}