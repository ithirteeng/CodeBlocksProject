package com.example.codeblocksproject

import Lexer
import Parser
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.codeblocksproject.databinding.FragmentMainBinding
import com.example.codeblocksproject.model.*
import com.example.codeblocksproject.ui.UserInterfaceClass


class MainFragment : Fragment(R.layout.fragment_main), MainFragmentInterface {
    companion object {
        const val PINK_COLOR = "pink"
        const val CHOCOLATE_COLOR = "chocolate"
        const val SPACE_COLOR = "space"
        const val MONOCHROME_COLOR = "monochrome"
        const val SHREK_COLOR = "shrek"
        const val INDENT = 100
    }

    private var cyclesCount = 0
    private val blockList: MutableList<CustomView> = mutableListOf()
    private val blockMap: MutableMap<Int, CustomView> = mutableMapOf()
    private val consoleFragment = ConsoleFragment()
    private val blocksFragment = BlocksFragment()
    private lateinit var binding: FragmentMainBinding
    private var startBlockID = 0
    private var endBlockID = 0
    private var freeId = 0
    private lateinit var draggingBlock: CustomView
    private var draggingList = mutableListOf<CustomView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ui = UserInterfaceClass(view.context, consoleFragment, blocksFragment)
        ui.setupAllUIFunctions(view)
        setupOtherFragmentsFunctions()
        setupAllDragListeners()
        binding.zoomLayout.zoomTo(4f, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val startBlock: StartProgramBlock = binding.startProgram
        startBlockID = binding.startProgram.blockView.id

        blockList.add(startBlock)
        blockMap[startBlockID] = startBlock
        startBlock.position = 0
        startBlock.blockView.setOnDragListener(choiceDragListener())

        val endBlock = binding.endProgram
        endBlockID = binding.endProgram.blockView.id

        blockList.add(endBlock)
        blockMap[endBlockID] = endBlock
        endBlock.position = 1

        startBlock.nextId = endBlockID
        endBlock.previousId = startBlockID

        return binding.root
    }

    private fun setupAllDragListeners() {
        binding.mainWorkfield.setOnDragListener(choiceDragListener())
        binding.drawerOutsideButton.setOnDragListener(choiceDragListener())
        binding.blocksButton.setOnDragListener(choiceDragListener())
        binding.startButton.setOnDragListener(choiceDragListener())
        binding.container.setOnDragListener(choiceDragListener())
    }

    private fun setupOtherFragmentsFunctions() {
        addingFragments()
        blocksButtonEvent()
        startButtonEvent()
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

    private fun blocksButtonEvent() {
        binding.blocksButton.setOnClickListener {
            if (consoleFragment.getIsClosedStart()) {
                if (blocksFragment.getIsClosedBlocks()) {
                    blocksFragment.setISClosedBlocks(false)
                    val fragmentManager = childFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                    blocksFragment.onCreateAnimation(0, true, 1)
                    transaction.show(blocksFragment)
                    transaction.commit()

                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            binding.blocksButton.visibility = View.GONE
                            binding.startButton.visibility = View.GONE
                        }
                    }, 350)
                }
            }
        }
    }

    private fun startButtonEvent() {
        binding.startButton.setOnClickListener {
            if (blocksFragment.getIsClosedBlocks()) {
                if (consoleFragment.getIsClosedStart()) {
                    consoleFragment.setISClosedStart(false)
                    val fragmentManager = childFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
                    consoleFragment.onCreateAnimation(0, true, 1)
                    transaction.show(consoleFragment)
                    transaction.commit()

                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            binding.startButton.visibility = View.GONE
                            binding.blocksButton.visibility = View.GONE
                        }
                    }, 350)
                    try {
                        checkIfBlocksNull()

                        val lexer = Lexer(blocksToCode(), DEBUG = true)
                        val tokens = lexer.lexicalAnalysis()

                        tokens.forEach { x -> println(x.aboutMe()) }
                        var answer = ""
                        val parser = Parser(tokens, DEBUG = true)
                        val array = parser.run()

                        for (string in array) {
                            if (string != "") {
                                answer += "$string\n"
                            }
                        }
                        consoleFragment.resultsToConsole(answer)
                    } catch (e: Exception) {
                        consoleFragment.resultsToConsole(e.message.toString())
                    }


                }
            }
        }
    }

    private fun checkIfBlocksNull() {
        for (index in 0 until blockList.size) {
            if (blockList[index].ifTextViewEmpty()) {
                throw Exception("Check ${blockList[index].blockType} it is empty!")
            }
        }
    }

    override fun displayButtons() {
        binding.startButton.visibility = View.VISIBLE
        binding.blocksButton.visibility = View.VISIBLE
    }

    override fun addBlock(type: String) {
        when (type) {
            BlockTypes.INIT_BLOCK_TYPE -> createBlock(InitializationBlock(requireContext()))
            BlockTypes.ASSIGN_BLOCK_TYPE -> createBlock(AssignmentBlock(requireContext()))
            BlockTypes.OUTPUT_BLOCK_TYPE -> createBlock(OutputBlock(requireContext()))
            BlockTypes.WHILE_BLOCK_TYPE -> {
                cyclesCount++
                makeMarginsForBlocks()
                createBlock(WhileBlock(requireContext()))
                createBlock(BeginBlock(requireContext()))
                createBlock(EndBlock(requireContext()))
            }
        }
        alignX()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createBlock(block: CustomView) {
        val lastBlock = blockMap[blockMap[endBlockID]!!.previousId]!!
        block.blockView.setDefault(lastBlock.blockView.x)
        lastBlock.nextId = block.blockView.id
        block.previousId = lastBlock.blockView.id

        blockMap[endBlockID]!!.previousId = block.blockView.id
        block.nextId = endBlockID

        blockList.add(block)
        blockMap[block.blockView.id] = block

        binding.mainWorkfield.addView(block.blockView, lastBlock.position + 1)
        block.blockView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        block.position = lastBlock.position + 1
        blockMap[endBlockID]!!.position++

        if (block.blockType != BlockTypes.BEGIN_BLOCK_TYPE && block.blockType != BlockTypes.END_BLOCK_TYPE)
            block.blockView.setOnLongClickListener(choiceLongClickListener())
        block.blockView.setOnDragListener(choiceDragListener())
        makeMarginsForBlocks()

    }

    private fun View.setDefault(x: Float) {
        this.x = x
        this.z = 1F
        this.id = freeId
        freeId++
        if (freeId == startBlockID || freeId == endBlockID)
            freeId++
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun choiceLongClickListener() = View.OnLongClickListener { view ->
        makeKeymapHidden()
        makeAllEditTextsDisabled()
        draggingList.clear()

        val currentBlock = blockMap[view.id]
        draggingList.add(currentBlock!!)
        removeNestedBlocks(currentBlock)
        val block = draggingList[draggingList.size - 1]
        if (currentBlock.previousId != -1) {
            blockMap[currentBlock.previousId]!!.nextId = block.nextId
        }
        if (block.nextId != -1) {
            blockMap[block.nextId]!!.previousId = currentBlock.previousId
        }
        view.visibility = View.INVISIBLE

        binding.mainWorkfield.removeView(view)
        binding.mainWorkfield.addView(view)

        refreshPositions()

        val data = ClipData.newPlainText("", "")
        val shadowBuilder = DragShadowBuilder(blockMap[view.id]!!.blockView)
        view.startDragAndDrop(data, shadowBuilder, view, 0)
        draggingBlock = blockMap[view.id]!!
        true
    }

    private fun choiceDragListener() = View.OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                binding.mainWorkfield.invalidate()
                makeKeymapHidden()
            }
            DragEvent.ACTION_DROP -> {
                if (blockMap[view.id] != null) {
                    draggingBlock.blockView.z = 1F
                    val currentBlock = draggingBlock
                    var block = blockMap[view.id]
                    if (blockMap[view.id]!!.isNestingPossible) {
                        block = blockMap[blockMap[view.id]!!.nextId]!!
                    }

                    val temp = block!!.nextId
                    block.nextId = currentBlock.blockView.id
                    currentBlock.previousId = block.blockView.id

                    val lastBlock = draggingList[draggingList.size - 1]
                    lastBlock.nextId = temp
                    blockMap[temp]!!.previousId = lastBlock.blockView.id

                    refreshPositions()

                    binding.mainWorkfield.removeView(currentBlock.blockView)
                    for (dragBlock in draggingList) {
                        binding.mainWorkfield.addView(
                            dragBlock.blockView,
                            dragBlock.position
                        )
                    }
                } else {
                    if (draggingBlock.blockType == BlockTypes.WHILE_BLOCK_TYPE ||
                        draggingBlock.blockType == BlockTypes.IF_BLOCK_TYPE
                    ) {
                        cyclesCount--
                    }
                    for (block in draggingList) {
                        deleteBlock(block)
                    }
                }
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                draggingBlock.blockView.post {
                    draggingBlock.blockView.visibility = View.VISIBLE
                }
                alignX()
            }
        }
        true
    }

    private fun refreshPositions() {
        var block = blockMap[startBlockID]!!
        block.position = 0
        var ind = 1
        while (block.blockView.id != endBlockID) {
            block = blockMap[block.nextId]!!
            block.position = ind
            ind++
        }
    }

    private fun makeAllEditTextsDisabled() {
        for (block in blockList) {
            if (block.blockView.id != endBlockID && block.blockView.id != startBlockID)
                block.makeEditTextsDisabled()
        }
    }

    private fun makeKeymapHidden() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.mainWorkfield.windowToken, 0)
    }

    private fun blocksToCode(): String {
        var code = ""
        var currentBlock = blockMap[startBlockID]

        while (currentBlock!!.blockView.id != endBlockID) {
            if (currentBlock.blockView.id != startBlockID) {
                code += currentBlock.blockToCode() + "\n"
            }
            currentBlock = blockMap[currentBlock.nextId]!!


        }
        return code
    }

    private fun alignX() {
        var prevBlock = blockMap[startBlockID]
        var currentBlock = prevBlock

        while (currentBlock!!.blockView.id != endBlockID) {
            currentBlock = blockMap[prevBlock!!.nextId]!!
            currentBlock.blockView.x = prevBlock.blockView.x

            if (prevBlock.isNestingPossible) {
                currentBlock.blockView.x += INDENT
            }
            if (prevBlock.blockType == BlockTypes.END_BLOCK_TYPE) {
                currentBlock.blockView.x -= INDENT
            }

            prevBlock = currentBlock
        }
    }

    private fun deleteBlock(block: CustomView) {
        binding.mainWorkfield.removeView(block.blockView)
        blockMap.remove(block.blockView.id)
        blockList.remove(block)
    }

    private fun removeNestedBlocks(parentBlock: CustomView) {
        var brackets = 0
        var block = blockMap[parentBlock.nextId]
        if (block!!.blockType == BlockTypes.BEGIN_BLOCK_TYPE) {
            brackets++
            draggingList.add(block)
            binding.mainWorkfield.removeView(block.blockView)
        }
        while (brackets != 0) {
            block = blockMap[block!!.nextId]!!
            draggingList.add(block)
            binding.mainWorkfield.removeView(block.blockView)

            if (block.blockType == BlockTypes.BEGIN_BLOCK_TYPE) {
                brackets++
            }
            if (block.blockType == BlockTypes.END_BLOCK_TYPE) {
                brackets--
            }
        }
        if (block!!.nextId != -1)
            block = blockMap[block.nextId]
        if (block!!.blockType == BlockTypes.ELSE_BLOCK_TYPE) {
            draggingList.add(block)
            binding.mainWorkfield.removeView(block.blockView)
            removeNestedBlocks(block)
        }
    }

    private fun makeMarginsForBlocks() {
        for (block in blockList) {
            val params: LinearLayout.LayoutParams =
                block.blockView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, cyclesCount * INDENT, 0)
        }
    }
}