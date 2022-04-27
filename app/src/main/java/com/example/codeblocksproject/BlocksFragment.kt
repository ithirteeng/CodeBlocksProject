package com.example.codeblocksproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class BlocksFragment : Fragment(R.layout.fragment_blocks) {
    var isClosedBlocks = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeSlidingFragment(view)
    }

    private fun closeSlidingFragment(view: View) {
        val button = view.findViewById<Button>(R.id.closeButton)
        button.setOnClickListener {
            val factory = layoutInflater
            val mainFragmentView = factory.inflate(R.layout.fragment_main, null)
            mainFragmentView.findViewById<Button>(R.id.startButton).visibility = View.VISIBLE
            mainFragmentView.findViewById<Button>(R.id.blocksButton).visibility = View.VISIBLE

            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(0, R.anim.bottom_panel_slide_in)
            this.onCreateAnimation(0, true, 1)
            transaction.remove(this)
            transaction.commit()
            isClosedBlocks = true
        }
    }

}