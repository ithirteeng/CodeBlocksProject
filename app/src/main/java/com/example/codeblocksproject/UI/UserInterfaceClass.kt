package com.example.codeblocksproject.UI

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.codeblocksproject.BlocksFragment
import com.example.codeblocksproject.MainFragment
import com.example.codeblocksproject.R
import com.example.codeblocksproject.ConsoleFragment

class UserInterfaceClass(
    private val context: Context,
    private val consoleFragment: ConsoleFragment,
    private val blocksFragment: BlocksFragment
) {
    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }

    fun setupAllUIFunctions(view: View) {
        setupButtonsEvents(view)

        view.findViewById<DrawerLayout>(R.id.drawerLayout)
            .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun setupButtonsEvents(view: View) {
        drawerButtonEvent(view)
        insideDrawerButtonEvent(view)
        changeThemesEvent(view)
    }

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun drawerButtonEvent(view: View) {
        val button = view.findViewById<ImageButton>(R.id.drawerOutsideButton)
        button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(Gravity.LEFT)
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
                    view.findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(Gravity.LEFT)
            }
            false
        }
    }

    private fun changeThemesEvent(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.themesGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val button = view.findViewById<RadioButton>(checkedId)
            button?.apply {
                when (checkedId) {
                    R.id.spaceTheme -> {
                        setupChangeColorsFunctions(view, MainFragment.SPACE_COLOR, this.context)
                    }
                    R.id.pinkTheme -> {
                        setupChangeColorsFunctions(view, MainFragment.PINK_COLOR, this.context)
                    }
                    R.id.chocolateTheme -> {
                        setupChangeColorsFunctions(view, MainFragment.CHOCOLATE_COLOR, this.context)
                    }
                    R.id.sepiaTheme -> {
                        setupChangeColorsFunctions(view, MainFragment.SEPIA_COLOR, this.context)
                    }
                    R.id.shrekTheme -> {
                        setupChangeColorsFunctions(view, MainFragment.SHREK_COLOR, this.context)
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
        consoleFragment.changeTheme(color, context)
        blocksFragment.changeTheme(color, context)
    }

    private fun changeDrawerBackgroundColor(view: View, color: String) {
        val background = view.findViewById<ConstraintLayout>(R.id.drawerInsidesLayout)
        val shrekView = view.findViewById<ImageView>(R.id.shrekImage)
        shrekView.visibility = View.GONE
        when (color) {
            MainFragment.SPACE_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.spaceMainColor
                    )
                )
            }
            MainFragment.PINK_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pinkMainColor
                    )
                )
            }
            MainFragment.CHOCOLATE_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.chocolateMainColor
                    )
                )
            }
            MainFragment.SEPIA_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sepiaMainColor
                    )
                )
            }
            MainFragment.SHREK_COLOR -> {
                shrekView.visibility = View.VISIBLE
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.shrekMainColor
                    )
                )
            }
        }
    }

    private fun changeMainBackgroundColor(view: View, color: String) {
        val background = view.findViewById<ConstraintLayout>(R.id.container)
        when (color) {
            MainFragment.SPACE_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.spaceMainColor
                    )
                )
            }
            MainFragment.PINK_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pinkMainColor
                    )
                )
            }
            MainFragment.CHOCOLATE_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.chocolateMainColor
                    )
                )
            }
            MainFragment.SEPIA_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sepiaMainColor
                    )
                )
            }
            MainFragment.SHREK_COLOR -> {
                background.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.shrekMainColor
                    )
                )
            }
        }
    }

    private fun changeWorkfieldColor(view: View, color: String) {
        val imageView = view.findViewById<ImageView>(R.id.workspaceBackground)
        when (color) {
            MainFragment.SPACE_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.spaceWorkingPanelColor
                    )
                )
            }
            MainFragment.PINK_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pinkWorkingPanelColor
                    )
                )
            }
            MainFragment.CHOCOLATE_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.chocolateWorkingPanelColor
                    )
                )
            }
            MainFragment.SEPIA_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sepiaWorkingPanelColor
                    )
                )
            }
            MainFragment.SHREK_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.shrekWorkingPanelColor
                    )
                )
            }
        }
    }

    private fun changeBottomButtonsColor(view: View, color: String) {
        val imageView = view.findViewById<ImageView>(R.id.bottomButtonsBackground)
        val buttonStart = view.findViewById<Button>(R.id.startButton)
        val buttonBlocks = view.findViewById<Button>(R.id.blocksButton)
        buttonStart.setTextColor(getColor(R.color.chocolateMainColor, this.context))
        buttonBlocks.setTextColor(getColor(R.color.chocolateMainColor, this.context))
        when (color) {
            MainFragment.SPACE_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.spaceBottomButtonsColor
                    )
                )
            }
            MainFragment.PINK_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pinkBottomButtonsColor
                    )
                )
            }
            MainFragment.CHOCOLATE_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.chocolateBottomButtonsColor
                    )
                )
            }
            MainFragment.SEPIA_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sepiaBottomButtonsColor
                    )
                )
                buttonStart.setTextColor(getColor(R.color.white, this.context))
                buttonBlocks.setTextColor(getColor(R.color.white, this.context))
            }
            MainFragment.SHREK_COLOR -> {
                imageView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.shrekBottomButtonsColor
                    )
                )
            }
        }

    }

    private fun changeDrawerButtonColor(view: View, color: String) {
        var button = view.findViewById<ImageButton>(R.id.drawerButton)
        when (color) {
            MainFragment.PINK_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.pink_drawer_button_image,
                    null
                )
            )
            MainFragment.CHOCOLATE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.chocolate_drawer_button_image,
                    null
                )
            )
            MainFragment.SPACE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.space_drawer_button_image,
                    null
                )
            )
            MainFragment.SEPIA_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.sepia_drawer_button_image,
                    null
                )
            )
            MainFragment.SHREK_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.shrek_drawer_button_image,
                    null
                )
            )
        }
        button = view.findViewById(R.id.drawerOutsideButton)
        when (color) {
            MainFragment.PINK_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.pink_drawer_button_image,
                    null
                )
            )
            MainFragment.CHOCOLATE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.chocolate_drawer_button_image,
                    null
                )
            )
            MainFragment.SPACE_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.space_drawer_button_image,
                    null
                )
            )
            MainFragment.SEPIA_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.sepia_drawer_button_image,
                    null
                )
            )
            MainFragment.SHREK_COLOR -> button.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.shrek_drawer_button_image,
                    null
                )
            )
        }
    }
}
