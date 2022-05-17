package com.example.codeblocksproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codeblocksproject.databinding.FragmentConsoleBinding

class ConsoleFragment : Fragment(R.layout.fragment_console) {
    private var isClosedStart = true
    private lateinit var binding: FragmentConsoleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeSlidingFragment()
    }

    fun getIsClosedStart(): Boolean {
        return isClosedStart
    }

    fun setISClosedStart(meaning: Boolean) {
        isClosedStart = meaning
    }

    fun resultsToConsole(s: String) {
        binding.consoleTextView.text = s
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun closeSlidingFragment() {
        binding.closeButton.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(0, R.anim.bottom_panel_slide_in)
            this.onCreateAnimation(0, true, 1)
            transaction.hide(this)
            transaction.commit()
            isClosedStart = true
            Handler(Looper.getMainLooper()).postDelayed({
                kotlin.run {
                    try {
                        (parentFragment as WorkspaceFragment).displayButtons()
                    } catch (e: Exception) {
                        (parentFragment as TextCodingFragment).displayButtons()
                    }
                }
            }, 350)

        }
    }

    fun changeTheme(color: String, context: Context) {
        binding.closeButton.setTextColor(getColor(R.color.chocolateMainColor, context))

        when (color) {
            WorkspaceFragment.SPACE_COLOR -> {
                changeColor(
                    R.color.spaceConsoleColor,
                    R.color.chocolateMainColor,
                    R.color.spaceBottomButtonsColor,
                    context
                )
            }
            WorkspaceFragment.PINK_COLOR -> {
                changeColor(
                    R.color.pinkConsoleColor,
                    R.color.chocolateMainColor,
                    R.color.pinkBottomButtonsColor,
                    context
                )
            }
            WorkspaceFragment.CHOCOLATE_COLOR -> {
                changeColor(
                    R.color.chocolateConsoleColor,
                    R.color.black,
                    R.color.chocolateBottomButtonsColor,
                    context
                )
            }
            WorkspaceFragment.MONOCHROME_COLOR -> {
                changeColor(
                    R.color.monochromeConsoleColor,
                    R.color.white,
                    R.color.monochromeBottomButtonsColor,
                    context
                )
                binding.closeButton.setTextColor(getColor(R.color.white, context))
            }
            WorkspaceFragment.SHREK_COLOR -> {
                changeColor(
                    R.color.shrekConsoleColor,
                    R.color.black,
                    R.color.shrekBottomButtonsColor,
                    context
                )
            }

        }
    }

    private fun changeColor(
        backgroundColor: Int,
        textColor: Int,
        buttonsColor: Int,
        context: Context
    ) {
        binding.consoleTextView.setBackgroundColor(getColor(backgroundColor, context))
        binding.consoleTextView.setTextColor(getColor(textColor, context))

        binding.buttonBackground.setBackgroundColor(getColor(buttonsColor, context))
    }

    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }

}