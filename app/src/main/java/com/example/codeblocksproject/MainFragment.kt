package com.example.codeblocksproject

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.codeblocksproject.databinding.FragmentMainBinding
import com.example.codeblocksproject.model.CustomView
import com.example.codeblocksproject.model.EndProgramBlock
import com.example.codeblocksproject.model.InitializationBlock
import com.example.codeblocksproject.model.StartProgramBlock
import com.example.codeblocksproject.ui.UserInterfaceClass

class MainFragment : Fragment(R.layout.fragment_main), MainFragmentInterface {
    companion object {
        const val PINK_COLOR = "pink"
        const val CHOCOLATE_COLOR = "chocolate"
        const val SPACE_COLOR = "space"
        const val MONOCHROME_COLOR = "monochrome"
        const val SHREK_COLOR = "shrek"
    }

    private val blockList: MutableList<CustomView> = mutableListOf()
    private val blockMap: MutableMap<Int, CustomView> = mutableMapOf()
    private val consoleFragment = ConsoleFragment()
    private val blocksFragment = BlocksFragment()
    private lateinit var binding: FragmentMainBinding
    private val workFieldRect = Rect()
    private var startBlockID = 0
    private var endBlockID = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ui = UserInterfaceClass(view.context, consoleFragment, blocksFragment)
        ui.setupAllUIFunctions(view)
        setupOtherFragmentsFunctions(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.mainWorkfield.getHitRect(workFieldRect)

        val startBlock = StartProgramBlock(binding.start, requireContext())
        startBlockID = binding.start.id
        blockList.add(startBlock)
        blockMap[startBlockID] = startBlock

        val endBlock = EndProgramBlock(binding.end, requireContext())
        endBlockID = binding.end.id
        blockList.add(endBlock)
        blockMap[endBlockID] = endBlock

        startBlock.nextId = endBlockID
        endBlock.previousId = startBlockID

        return binding.root
    }

    private fun setupOtherFragmentsFunctions(view: View) {
        addingFragments()
        blocksButtonEvent(view)
        startButtonEvent(view)
    }

    private fun addingFragments() {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.container, consoleFragment)
        transaction.add(R.id.container, blocksFragment)
        transaction.hide(consoleFragment)
        transaction.hide(blocksFragment)
        transaction.commit()
    }

    private fun blocksButtonEvent(view: View) {
        val button = view.findViewById<Button>(R.id.blocksButton)
        button.setOnClickListener {
            if (consoleFragment.isClosedStart) {
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
                if (consoleFragment.isClosedStart) {
                    consoleFragment.isClosedStart = false
                    val fragmentManager = childFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                    consoleFragment.onCreateAnimation(0, true, 1)
                    transaction.show(consoleFragment)
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
        createView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createView() {
        val newBlock = InitializationBlock(requireContext())


        val lastBlock = blockMap[blockMap[endBlockID]!!.previousId]!!
        newBlock.setDefault(
            lastBlock.blockView.x,
            lastBlock.blockView.y + lastBlock.blockView.height
        )

        lastBlock.nextId = newBlock.blockView.id
        newBlock.previousId = lastBlock.blockView.id

        blockMap[endBlockID]!!.previousId = newBlock.blockView.id
        newBlock.nextId = endBlockID

        binding.mainWorkfield.addView(newBlock)

        newBlock.setOnTouchListener { view, event ->
            moveBlock(view, event)
        }

        blockList.add(newBlock)
        blockMap[newBlock.blockView.id] = newBlock
    }

    private fun View.setDefault(x: Float, y: Float) {
        this.x = x
        this.y = y
        this.z = 1F
        this.id = (2..1000000000).random()
    }

    private var x = 0F
    private var y = 0F
    private fun moveBlock(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            x = event.x
            y = event.y

            val currentBlock = blockMap[view.id]
            if (currentBlock!!.previousId != -1) {
                blockMap[currentBlock.previousId]!!.nextId = currentBlock.nextId
            }
            if (currentBlock.nextId != -1) {
                blockMap[currentBlock.nextId]!!.previousId = currentBlock.previousId
            }

            view.z = 2F
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            view.x += event.x - x
            view.y += event.y - y
        }
        if (event.action == MotionEvent.ACTION_UP) {
            view.z = 1F

            val currentBlock = blockMap[view.id]

            var block = blockMap[startBlockID]
            var isBlockNested=false

            while (block!!.blockView.id != endBlockID) {
                if (cross(block, currentBlock!!)) {
                    val temp = block.nextId
                    block.nextId = currentBlock.blockView.id
                    currentBlock.previousId = block.blockView.id
                    currentBlock.nextId = temp
                    blockMap[temp]!!.previousId = currentBlock.blockView.id

                    isBlockNested=true
                    break
                }
                block = blockMap[block.nextId]!!
            }

            val blockRect = Rect()
            view.getHitRect(blockRect)
            if (!isBlockNested) {
                deleteView(currentBlock!!)
            }
        }
        alignBlock(blockMap[startBlockID]!!)
        return true
    }

    private fun deleteView(block: CustomView) {
        binding.mainWorkfield.removeView(block.blockView)
        blockMap.remove(block.blockView.id)
        blockList.remove(block)
    }

    private fun alignBlock(block: CustomView) {

        var childBlock = block
        var parentBlock = block

        while (childBlock.blockView.id != endBlockID) {
            childBlock = blockMap[childBlock.nextId]!!
            childBlock.blockView.x = parentBlock.blockView.x
            childBlock.blockView.y = parentBlock.blockView.y + parentBlock.blockView.height

            parentBlock = childBlock
        }
    }

    private fun cross(parent: CustomView, child: CustomView): Boolean {
        val rectParent = Rect()
        val rectChild = Rect()
        parent.blockView.getHitRect(rectParent)
        child.blockView.getHitRect(rectChild)
        if (rectParent.intersect(rectChild)) {
            return true
        }
        return false
    }

    fun blocksToCode(): String {
        var code = "{"
        var currentBlock = blockMap[startBlockID]

        while (currentBlock!!.blockView.id != endBlockID) {
            code += currentBlock.blockToCode()
            currentBlock = blockMap[currentBlock.nextId]!!
        }
        code += "}"
        return code
    }

}