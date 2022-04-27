package com.example.codeblocksproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.Gravity.LEFT
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

class MainFragment : Fragment(R.layout.fragment_main), MainFragmentInterface {
    private val startFragment = StartFragment()
    private val blocksFragment = BlocksFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonsEvents(view)
        addingFragments()
    }

    private fun setupButtonsEvents(view: View) {
        blocksButtonEvent(view)
        startButtonEvent(view)
        drawerButtonEvent(view)
        //insideDrawerButtonEvent(view)
    }

    private fun addingFragments() {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.container, startFragment)
        transaction.add(R.id.container, blocksFragment)
        transaction.hide(startFragment)
        transaction.hide(blocksFragment)
        transaction.commit()
    }

    private fun blocksButtonEvent(view: View) {
        val button = view.findViewById<Button>(R.id.blocksButton)
        button.setOnClickListener {
            if (blocksFragment.isClosedBlocks) {
                blocksFragment.isClosedBlocks = false

                val fragmentManager = childFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                blocksFragment.onCreateAnimation(0, true, 1)
                transaction.show(blocksFragment)
                transaction.commit()

                Handler().postDelayed(Runnable {
                    kotlin.run {
                        button.visibility = View.GONE
                        view.findViewById<Button>(R.id.startButton).visibility = View.GONE
                    }
                }, 150)
            }
        }
    }

    private fun startButtonEvent(view: View) {
        val button = view.findViewById<Button>(R.id.startButton)
        button.setOnClickListener {
            if (startFragment.isClosedStart) {

                startFragment.isClosedStart = false

                val fragmentManager = childFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                startFragment.onCreateAnimation(0, true, 1)
                transaction.show(startFragment)
                transaction.commit()

                Handler().postDelayed(Runnable {
                    kotlin.run {
                        button.visibility = View.GONE
                        view.findViewById<Button>(R.id.blocksButton).visibility = View.GONE
                    }
                }, 150)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun drawerButtonEvent(view: View) {
        val button = view.findViewById<ImageButton>(R.id.drawerOutsideButton)
        button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(LEFT)
                    button.visibility = View.INVISIBLE
                }
            }
            false

        }
    }

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun insideDrawerButtonEvent(view: View) {
        val button = view.findViewById<ImageButton>(R.id.drawerButton)
        button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN ->
                    view.findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(LEFT)
            }
            false
        }
    }

//    private fun drawerListener(view: View) {
//        val drawer = view.findViewById<DrawerLayout>(R.id.drawerLayout)
//        drawer.addDrawerListener()
//    }

    override fun displayButtons() {
        view?.findViewById<Button>(R.id.startButton)?.visibility = View.VISIBLE
        view?.findViewById<Button>(R.id.blocksButton)?.visibility = View.VISIBLE
    }
}
