package com.example.codeblocksproject

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity.LEFT
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

class MainFragment : Fragment(R.layout.fragment_main), MainFragmentInterface {
    companion object {
        const val PINK_COLOR = "pink"
        const val CHOCOLATE_COLOR = "chocolate"
        const val SPACE_COLOR = "space"
    }

    private val startFragment = StartFragment()
    private val blocksFragment = BlocksFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonsEvents(view)
        addingFragments()

        view.findViewById<DrawerLayout>(R.id.drawerLayout)
            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun setupButtonsEvents(view: View) {
        blocksButtonEvent(view)
        startButtonEvent(view)
        drawerButtonEvent(view)
        insideDrawerButtonEvent(view)
        changeThemesEvent(view)
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

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun drawerButtonEvent(view: View) {
        val button = view.findViewById<ImageButton>(R.id.drawerOutsideButton)
        button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(LEFT)
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

    override fun displayButtons() {
        view?.findViewById<Button>(R.id.startButton)?.visibility = View.VISIBLE
        view?.findViewById<Button>(R.id.blocksButton)?.visibility = View.VISIBLE
    }

    private fun changeThemesEvent(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.themesGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val button = view.findViewById<RadioButton>(checkedId)
            button?.apply {
                when (checkedId) {
                    R.id.spaceTheme -> {
                        setupChangeColorsFunctions(view, SPACE_COLOR, this.context)
                    }
                    R.id.pinkTheme -> {
                        setupChangeColorsFunctions(view, PINK_COLOR, this.context)
                    }
                    R.id.chocolateTheme -> {
                        setupChangeColorsFunctions(view, CHOCOLATE_COLOR, this.context)
                    }
                }
            }
        }
    }

    private fun setupChangeColorsFunctions(view: View, color: String, context: Context) {
        changeMainBackgroundColor(view, color)
        changeWorkfieldColor(view, color)
        changeDrawerButtonColor(view, color)
        changeDrawerBackgroundColor(view, color)
        changeBottomButtonsColor(view, color)
        startFragment.changeTheme(color, context)
        blocksFragment.changeTheme(color, context)
    }

    private fun changeDrawerBackgroundColor(view: View, color: String) {
        val background = view.findViewById<ConstraintLayout>(R.id.drawerInsidesLayout)
        when (color) {
            SPACE_COLOR -> {
                background.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.spaceMainColor
                    )
                )
            }
            PINK_COLOR -> {
                background.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.pinkMainColor
                    )
                )
            }
            CHOCOLATE_COLOR -> {
                background.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.chocolateMainColor
                    )
                )
            }
        }
    }

    private fun changeMainBackgroundColor(view: View, color: String) {
        val background = view.findViewById<ConstraintLayout>(R.id.container)
        when (color) {
            SPACE_COLOR -> {
                background.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.spaceMainColor
                    )
                )
            }
            PINK_COLOR -> {
                background.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.pinkMainColor
                    )
                )
            }
            CHOCOLATE_COLOR -> {
                background.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.chocolateMainColor
                    )
                )
            }
        }
    }

    private fun changeWorkfieldColor(view: View, color: String) {
        val imageView = view.findViewById<ImageView>(R.id.workspaceBackground)
        when (color) {
            SPACE_COLOR -> {
                imageView.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.spaceWorkingPanelColor
                    )
                )
            }
            PINK_COLOR -> {
                imageView.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.pinkWorkingPanelColor
                    )
                )
            }
            CHOCOLATE_COLOR -> {
                imageView.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.chocolateWorkingPanelColor
                    )
                )
            }
        }
    }

    private fun changeBottomButtonsColor(view: View, color: String) {
        val imageView = view.findViewById<ImageView>(R.id.bottomButtonsBackground)
        when (color) {
            SPACE_COLOR -> {
                imageView.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.spaceBottomButtonsColor
                    )
                )
            }
            PINK_COLOR -> {
                imageView.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.pinkBottomButtonsColor
                    )
                )
            }
            CHOCOLATE_COLOR -> {
                imageView.setBackgroundColor(
                    getColor(
                        requireContext(),
                        R.color.chocolateBottomButtonsColor
                    )
                )
            }
        }

    }

    private fun changeDrawerButtonColor(view: View, color: String) {
        var button = view.findViewById<ImageButton>(R.id.drawerButton)
        when (color) {
            PINK_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.pink_drawer_button_image,
                    null
                )
            )
            CHOCOLATE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.chocolate_drawer_button_image,
                    null
                )
            )
            SPACE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.space_drawer_button_image,
                    null
                )
            )
        }
        button = view.findViewById(R.id.drawerOutsideButton)
        when (color) {
            PINK_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.pink_drawer_button_image,
                    null
                )
            )
            CHOCOLATE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.chocolate_drawer_button_image,
                    null
                )
            )
            SPACE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.space_drawer_button_image,
                    null
                )
            )
        }
    }
}
