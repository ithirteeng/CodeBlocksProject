package com.example.codeblocksproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButtonEvent(view)
        blocksButtonEvent(view)

        val factory = layoutInflater
        val startView = factory.inflate(R.layout.start_button_fragment, null)
        val startFragment = StartFragment()

    }


    private fun blocksButtonEvent(view: View) {
        val button = view.findViewById<Button>(R.id.blocksButton)
        val blocksFragment = BlocksFragment()

        button.setOnClickListener {
            if (blocksFragment.isClosedBlocks) {
                button.visibility = View.GONE
                view.findViewById<Button>(R.id.startButton).visibility = View.GONE

                blocksFragment.isClosedBlocks = false

                val fragmentManager = childFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                blocksFragment.onCreateAnimation(0, true, 1)
                transaction.add(R.id.container, blocksFragment)
                transaction.commit()
            }
        }
    }

    private fun startButtonEvent(view: View) {
        val button = view.findViewById<Button>(R.id.startButton)
        val startFragment = StartFragment()
        button.setOnClickListener {
            if (startFragment.isClosedStart) {
                button.visibility = View.GONE
                view.findViewById<Button>(R.id.blocksButton).visibility = View.GONE
                startFragment.isClosedStart = false
                val fragmentManager = childFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                startFragment.onCreateAnimation(0, true, 1)
                transaction.add(R.id.container, startFragment)
                transaction.commit()
            }
        }
    }
}