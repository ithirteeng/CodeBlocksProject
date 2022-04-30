package com.example.codeblocksproject

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment

class MainFragment : Fragment(R.layout.fragment_main), MainFragmentInterface {
    companion object {
        const val PINK_COLOR = "pink"
        const val CHOCOLATE_COLOR = "chocolate"
        const val SPACE_COLOR = "space"
//        const val SEPIA_COLOR = "sepia"
    }

    private val startFragment = StartFragment()
    private val blocksFragment = BlocksFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ui = UserInterfaceClass(view.context, startFragment, blocksFragment)
        ui.setupAllClassFunctions(view)
        setupOtherFragmentsFunctions(view)
    }

    private fun setupOtherFragmentsFunctions(view: View) {
        addingFragments()
        blocksButtonEvent(view)
        startButtonEvent(view)
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
            if (startFragment.isClosedStart) {
                if (blocksFragment.isClosedBlocks) {
                    blocksFragment.isClosedBlocks = false
                    val fragmentManager = childFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                    blocksFragment.onCreateAnimation(0, true, 1)
                    transaction.show(blocksFragment)
                    transaction.commit()

                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            button.visibility = View.GONE
                            view.findViewById<Button>(R.id.startButton).visibility = View.GONE
                        }
                    }, 350)
                }
            }
        }
    }

    private fun startButtonEvent(view: View) {
        val button = view.findViewById<Button>(R.id.startButton)
        button.setOnClickListener {
            if (blocksFragment.isClosedBlocks) {
                if (startFragment.isClosedStart) {
                    startFragment.isClosedStart = false
                    val fragmentManager = childFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                    startFragment.onCreateAnimation(0, true, 1)
                    transaction.show(startFragment)
                    transaction.commit()

                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            button.visibility = View.GONE
                            view.findViewById<Button>(R.id.blocksButton).visibility = View.GONE
                        }
                    }, 350)
                }
            }
        }
    }

    override fun displayButtons() {
        view?.findViewById<Button>(R.id.startButton)?.visibility = View.VISIBLE
        view?.findViewById<Button>(R.id.blocksButton)?.visibility = View.VISIBLE
    }

    override fun addBlock() {
        val imageView = view?.findViewById<ImageView>(R.id.workspaceBackground)
        imageView?.setBackgroundColor(
            getColor(
                requireContext(),
                R.color.pinkMainColor
            )
        )
    }

}
