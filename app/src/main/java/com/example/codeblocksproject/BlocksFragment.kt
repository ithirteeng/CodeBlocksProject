package com.example.codeblocksproject

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.codeblocksproject.databinding.FragmentBlocksBinding
import com.example.codeblocksproject.model.BlockTypes
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class BlocksFragment : Fragment(R.layout.fragment_blocks) {
    private var isClosedBlocks = true
    private lateinit var binding: FragmentBlocksBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeSlidingFragment()
        keymapObserver()
        addingBlocks()
    }

    fun getIsClosedBlocks() = isClosedBlocks

    fun setIsClosedBlocks(meaning: Boolean) {
        isClosedBlocks = meaning
    }

    private fun addingBlocks() {
        binding.initializationBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.INIT_BLOCK_TYPE)
        }
        binding.assignmentBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.ASSIGN_BLOCK_TYPE)
        }
        binding.outputBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.OUTPUT_BLOCK_TYPE)
        }
        binding.whileBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.WHILE_BLOCK_TYPE)
        }
        binding.ifBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.IF_BLOCK_TYPE)
        }
        binding.elseBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.ELSE_BLOCK_TYPE)
        }
        binding.arrayInitBlock.setOnClickListener {
            (parentFragment as WorkspaceFragment).addBlock(BlockTypes.ARRAY_INIT_BLOCK_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlocksBinding.inflate(inflater, container, false)
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
            isClosedBlocks = true
            Handler(Looper.getMainLooper()).postDelayed({
                kotlin.run {
                    (parentFragment as WorkspaceFragment).displayButtons()
                }
            }, 350)
        }
    }

    private fun keymapObserver() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding.root.rootView.height

            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                binding.closeButton.performClick()
            }
        }
    }

    fun changeTheme(color: String, context: Context) {
        binding.closeButton.setTextColor(getColor(R.color.chocolateMainColor, context))

        when (color) {
            WorkspaceFragment.SPACE_COLOR -> {
                changeColor(
                    R.color.spaceConsoleColor,
                    R.color.spaceBottomButtonsColor,
                    context
                )
            }
            WorkspaceFragment.PINK_COLOR -> {
                changeColor(
                    R.color.pinkConsoleColor,
                    R.color.pinkBottomButtonsColor,
                    context
                )
            }
            WorkspaceFragment.CHOCOLATE_COLOR -> {
                changeColor(
                    R.color.chocolateConsoleColor,
                    R.color.chocolateBottomButtonsColor,
                    context
                )
            }
            WorkspaceFragment.MONOCHROME_COLOR -> {
                changeColor(
                    R.color.monochromeConsoleColor,
                    R.color.monochromeBottomButtonsColor,
                    context
                )
                binding.closeButton.setTextColor(getColor(R.color.white, context))
            }
            WorkspaceFragment.SHREK_COLOR -> {
                changeColor(
                    R.color.shrekConsoleColor,
                    R.color.shrekBottomButtonsColor,
                    context
                )
            }
        }
    }

    private fun changeColor(backgroundColor: Int, buttonColor: Int, context: Context) {
        binding.blocksBackground.setBackgroundColor(getColor(backgroundColor, context))
        binding.buttonBackground.setBackgroundColor(getColor(buttonColor, context))
    }

    private fun getColor(id: Int, context: Context): Int {
        return ContextCompat.getColor(context, id)
    }
}