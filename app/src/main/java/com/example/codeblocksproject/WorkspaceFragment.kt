package com.example.codeblocksproject

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.codeblocksproject.databinding.FragmentWorkspaceBinding
import com.example.codeblocksproject.interpreter.Lexer
import com.example.codeblocksproject.model.*
import com.example.codeblocksproject.ui.UserInterfaceClass
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class WorkspaceFragment : Fragment(R.layout.fragment_workspace) {
    companion object {
        const val PINK_COLOR = "pink"
        const val CHOCOLATE_COLOR = "chocolate"
        const val SPACE_COLOR = "space"
        const val MONOCHROME_COLOR = "monochrome"
        const val SHREK_COLOR = "shrek"
        const val INDENT = 100
        const val FILE_NAME = "blockProgram"
    }

    private lateinit var binding: FragmentWorkspaceBinding

    private val blockMap: MutableMap<Int, CustomView> = mutableMapOf()
    private var startBlockID = 0
    private var endBlockID = 0

    private val consoleFragment = ConsoleFragment()
    private val blocksFragment = BlocksFragment()

    private var cyclesCount = 0

    private var freeId = 0
    private lateinit var draggingBlock: CustomView
    private var draggingList = mutableListOf<CustomView>()
    private val ifBlockList = mutableListOf<CustomView>()
    private lateinit var prevIfBlock: CustomView

    private lateinit var programFile: ProgramFile
    var fileName = FILE_NAME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        programFile = ProgramFile(requireContext())
        binding = FragmentWorkspaceBinding.inflate(inflater, container, false)

        val startBlock: StartProgramBlock = binding.startProgram
        startBlockID = binding.startProgram.blockView.id

        blockMap[startBlockID] = startBlock
        startBlock.position = 0
        startBlock.blockView.setOnDragListener(choiceDragListener())

        val endBlock = binding.endProgram
        endBlockID = binding.endProgram.blockView.id

        blockMap[endBlockID] = endBlock
        endBlock.position = 1

        startBlock.nextId = endBlockID
        endBlock.previousId = startBlockID

        try {
            if (fileName.isEmpty())
                fileName = FILE_NAME
            val map = programFile.loadProgram(fileName)
            fillBlockMap(map)
            fillIfList()

            freeId = blockMap.size - 2
            loadToWorkfield()

            var block = blockMap[startBlockID]!!
            Log.i("ID---", block.blockType)
            while (block.blockView.id != endBlockID) {
                block = blockMap[block.nextId]!!
                Log.i("ID---", block.blockType)
            }
        } catch (e: Exception) {
            Log.i("---", e.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ui = UserInterfaceClass(view.context, consoleFragment, blocksFragment)
        ui.setupAllUIFunctions(view)
        setupOtherFragmentsFunctions()
        setupAllDragListeners()
        backToMenuButtonEvent()
        clearAllButtonEvent()
        binding.zoomLayout.zoomTo(4f, true)
    }

    override fun onPause() {
        saveData()
        super.onPause()
    }

    override fun onDestroy() {
        saveData()
        super.onDestroy()
    }

    fun saveData() {
        try {
            if (fileName.isEmpty())
                fileName = FILE_NAME
            programFile.saveProgram(blockMap, startBlockID, endBlockID, fileName)
            Log.i("onDestroy", "Completed")
        } catch (e: Exception) {
            Log.i("onDestroy", e.toString())
        }
    }

    private fun fillBlockMap(map: Map<Int, CustomView>) {

        for (block in map.values) {
            blockMap[block.blockView.id] = block
            if (block.blockType == BlockTypes.WHILE_BLOCK_TYPE || block.blockType == BlockTypes.IF_BLOCK_TYPE)
                cyclesCount++

            if (block.previousId == startBlockID) {
                blockMap[startBlockID]!!.nextId = block.blockView.id
            }
            if (block.nextId == endBlockID) {
                blockMap[endBlockID]!!.previousId = block.blockView.id
            }
        }

        refreshPositions()
    }

    private fun fillIfList() {
        var block = blockMap[startBlockID]
        while (block!!.nextId != endBlockID) {
            block = blockMap[block.nextId]
            if (block!!.blockType == BlockTypes.IF_BLOCK_TYPE && !isIfHaveElse(block)) {
                ifBlockList.add(blockMap[block.blockView.id + 2]!!)
            }
        }
    }

    private fun isIfHaveElse(block: CustomView): Boolean {
        val end = blockMap[block.blockView.id + 2]
        if (end!!.nextId !in blockMap.keys || blockMap[end.nextId]!!.blockType == BlockTypes.ELSE_BLOCK_TYPE) {
            return true
        }
        return false
    }

    private fun loadToWorkfield() {
        var block = blockMap[startBlockID]!!
        while (block.nextId != endBlockID) {
            block = blockMap[block.nextId]!!
            binding.mainWorkfield.addView(block.blockView, block.position)
            block.blockView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            if (block.blockType != BlockTypes.BEGIN_BLOCK_TYPE && block.blockType != BlockTypes.END_BLOCK_TYPE) {
                block.blockView.setOnLongClickListener(choiceLongClickListener())
            }
            block.blockView.setOnDragListener(choiceDragListener())
        }

        makeMarginsForBlocks()
        alignX()
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

    private fun openFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.bottom_panel_slide_out, 0)
        fragment.onCreateAnimation(0, true, 1)
        transaction.show(fragment)
        transaction.commit()

        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                bottomMenuVisibility(View.GONE)
            }
        }, 350)
    }

    private fun blocksButtonEvent() {
        binding.blocksButton.setOnClickListener {
            if (consoleFragment.getIsClosedStart()) {
                if (blocksFragment.getIsClosedBlocks()) {
                    blocksFragment.setIsClosedBlocks(false)
                    openFragment(blocksFragment)
                }
            }
        }
    }

    private fun startButtonEvent() {
        binding.startButton.setOnClickListener {
            if (blocksFragment.getIsClosedBlocks()) {
                if (consoleFragment.getIsClosedStart()) {
                    consoleFragment.setISClosedStart(false)
                    openFragment(consoleFragment)
                    startProgram()
                    consoleFragment.setStopProgramFlag(false)
                    consoleFragment.changeStopButtonIcon(false)
                }
            }
        }
    }

    private fun bottomMenuVisibility(visibility: Int) {
        binding.startButton.visibility = visibility
        binding.blocksButton.visibility = visibility
    }


    fun startProgram() {
        GlobalScope.launch {
            try {
                checkIfBlocksNull()

                val lexer = Lexer(blocksToCode(), DEBUG = false)
                val tokens = lexer.lexicalAnalysis()

                tokens.forEach { x -> println(x.aboutMe()) }
                val parser =
                    com.example.codeblocksproject.interpreter.Parser(
                        tokens,
                        DEBUG = false
                    )
                parser.run(consoleFragment)
            } catch (e: Exception) {
                consoleFragment.resultsToConsole(e.message.toString())
            }
        }
    }


    fun displayButtons() {
        bottomMenuVisibility(View.VISIBLE)
    }

    private fun backToMenuButtonEvent() {
        view?.findViewById<Button>(R.id.backToMainButton)?.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
            saveData()
        }
    }

    private fun clearWorkfield(isRemovingOn: Boolean = true) {
        val startBlock = blockMap[startBlockID]
        val endBlock = blockMap[endBlockID]

        for (block in blockMap.values) {
            if (block.blockType != BlockTypes.START_PROGRAM_BLOCK_TYPE &&
                block.blockType != BlockTypes.END_PROGRAM_BLOCK_TYPE
            ) {
                binding.mainWorkfield.removeView(block.blockView)
            }
        }

        if (isRemovingOn) {
            freeId = 0
            blockMap.clear()
            ifBlockList.clear()

            blockMap[startBlockID] = startBlock!!
            blockMap[endBlockID] = endBlock!!

            startBlock.nextId = endBlockID
            endBlock.previousId = startBlockID
        }
    }

    private fun clearAllButtonEvent() {
        view?.findViewById<Button>(R.id.clearAllButton)?.setOnClickListener {
            clearWorkfield()
            saveData()
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.clearToast),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    fun addBlock(type: String) {
        when (type) {
            BlockTypes.INIT_BLOCK_TYPE -> createBlock(InitializationBlock(requireContext()))
            BlockTypes.ASSIGN_BLOCK_TYPE -> createBlock(AssignmentBlock(requireContext()))
            BlockTypes.ARRAY_INIT_BLOCK_TYPE -> createBlock(ArrayInitBlock(requireContext()))
            BlockTypes.OUTPUT_BLOCK_TYPE -> createBlock(OutputBlock(requireContext()))
            BlockTypes.WHILE_BLOCK_TYPE -> {
                cyclesCount++
                makeMarginsForBlocks()
                createBlock(WhileBlock(requireContext()))
                createBlock(BeginBlock(requireContext()))
                createBlock(EndBlock(requireContext()))
            }
            BlockTypes.IF_BLOCK_TYPE -> {
                cyclesCount++
                makeMarginsForBlocks()
                createBlock(IfBlock(requireContext()))
                createBlock(BeginBlock(requireContext()))

                val newEndBlock = EndBlock(requireContext())
                createBlock(newEndBlock)
                ifBlockList.add(newEndBlock)
            }
            BlockTypes.ELSE_BLOCK_TYPE -> {
                if (lastFreeIf() != null) {
                    createBlock(ElseBlock(requireContext()), lastFreeIf())
                }
            }
        }
        alignX()
    }

    private fun View.setDefault(x: Float) {
        this.x = x
        this.z = 1F
        this.id = freeId
        freeId++
    }

    private fun createBlock(block: CustomView, prevBlock: CustomView? = null) {

        val lastBlock: CustomView = prevBlock ?: blockMap[blockMap[endBlockID]!!.previousId]!!
        val endBlock = blockMap[lastBlock.nextId]!!

        block.blockView.setDefault(lastBlock.blockView.x)
        lastBlock.nextId = block.blockView.id
        block.previousId = lastBlock.blockView.id

        endBlock.previousId = block.blockView.id
        block.nextId = endBlock.blockView.id

        blockMap[block.blockView.id] = block

        refreshPositions()

        binding.mainWorkfield.addView(block.blockView, block.position)
        block.blockView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        if (block.blockType != BlockTypes.BEGIN_BLOCK_TYPE && block.blockType != BlockTypes.END_BLOCK_TYPE) {
            block.blockView.setOnLongClickListener(choiceLongClickListener())
        }
        block.blockView.setOnDragListener(choiceDragListener())
        makeMarginsForBlocks()

        if (block.blockType == BlockTypes.ELSE_BLOCK_TYPE) {
            createBlock(BeginBlock(requireContext()), block)
            createBlock(EndBlock(requireContext()), blockMap[block.nextId])
        }
    }


    private fun choiceLongClickListener() = View.OnLongClickListener { view ->
        makeKeymapHidden()
        makeAllEditTextsDisabled()
        draggingList.clear()

        val currentBlock = blockMap[view.id]
        if (currentBlock!!.blockType == BlockTypes.ELSE_BLOCK_TYPE)
            prevIfBlock = blockMap[currentBlock.previousId]!!
        draggingList.add(currentBlock)
        if (currentBlock.isNestingPossible)
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
                    var block = blockMap[view.id]
                    if (blockMap[view.id]!!.isNestingPossible) {
                        block = blockMap[blockMap[view.id]!!.nextId]!!
                    }

                    if (draggingBlock.blockType == BlockTypes.ELSE_BLOCK_TYPE) {
                        if (!ifBlockList.contains(block) ||
                            blockMap[block!!.nextId]!!.blockType == BlockTypes.ELSE_BLOCK_TYPE
                        ) {
                            block = prevIfBlock
                        }
                    } else {
                        if (ifBlockList.contains(block) &&
                            blockMap[block!!.nextId]!!.blockType == BlockTypes.ELSE_BLOCK_TYPE
                        ) {
                            block = endOfElse(blockMap[block.nextId]!!)
                        }
                    }

                    draggingBlock.blockView.z = 1F
                    val currentBlock = draggingBlock


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

    private fun endOfElse(elseBlock: CustomView): CustomView {
        var brackets = 1
        var block = blockMap[elseBlock.nextId]
        while (brackets != 0) {
            block = blockMap[block!!.nextId]
            if (block!!.blockType == BlockTypes.BEGIN_BLOCK_TYPE) {
                brackets++
            }
            if (block.blockType == BlockTypes.END_BLOCK_TYPE) {
                brackets--
            }
        }
        return block!!
    }

    private fun lastFreeIf(): CustomView? {
        var lastBlock: CustomView? = null
        refreshList()
        for (block in ifBlockList) {
            if (blockMap[block.nextId]!!.blockType != BlockTypes.ELSE_BLOCK_TYPE) {
                lastBlock = block
            }
        }
        return lastBlock
    }

    private fun refreshList() {
        val list = mutableListOf<CustomView>()
        for (block in ifBlockList) {
            if (blockMap.containsKey(block.blockView.id)) {
                list.add(block)
            }
        }

        ifBlockList.clear()
        ifBlockList.addAll(list)
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

    private fun checkIfBlocksNull() {
        var tempBlock = blockMap[startBlockID]
        var counter = 0
        while (tempBlock!!.blockView.id != endBlockID) {
            counter++
            if (tempBlock.ifTextViewEmpty()) {
                throw Exception("check ${tempBlock.blockType} block, it is empty: ($counter line)")
            }
            tempBlock = blockMap[tempBlock.nextId]
        }
    }

    private fun makeAllEditTextsDisabled() {
        for (block in blockMap.values) {
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
        var code = "{"
        var currentBlock = blockMap[startBlockID]

        while (currentBlock!!.blockView.id != endBlockID) {
            currentBlock = blockMap[currentBlock.nextId]!!
            code += "\n" + currentBlock.blockToCode()
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
        for (block in blockMap.values) {
            val params: LinearLayout.LayoutParams =
                block.blockView.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, cyclesCount * INDENT, 0)
        }
    }
}