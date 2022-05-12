package com.example.codeblocksproject

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.Rect
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
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.codeblocksproject.databinding.FragmentMainBinding
import com.example.codeblocksproject.model.*
import com.example.codeblocksproject.ui.UserInterfaceClass
import kotlin.math.abs


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
    private var freeId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ui = UserInterfaceClass(view.context, consoleFragment, blocksFragment)
        ui.setupAllUIFunctions(view)
        setupOtherFragmentsFunctions()
        setupAllDragListeners()
        binding.zoomLayout.zoomTo(4f, true)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding.root.rootView.height

            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                Log.i("keyboard", "open")
            }
        }
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
        startBlock.position = 0
        startBlock.blockView.setOnDragListener(choiceDragListener())

        val endBlock = EndProgramBlock(binding.end, requireContext())
        endBlockID = binding.end.id
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
                            binding.startButton.visibility = View.GONE
                            binding.blocksButton.visibility = View.GONE
                        }
                    }, 350)
                    consoleFragment.checkCode(blocksToCode())
                }
            }
        }
    }

    override fun displayButtons() {
        binding.startButton.visibility = View.VISIBLE
        binding.blocksButton.visibility = View.VISIBLE
    }

    override fun addBlock() {
        createBlock()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createBlock() {
        val newBlock = InitializationBlock(requireContext())

        val lastBlock = blockMap[blockMap[endBlockID]!!.previousId]!!
        newBlock.setDefault(lastBlock.blockView.x)
        lastBlock.nextId = newBlock.blockView.id
        newBlock.previousId = lastBlock.blockView.id

        blockMap[endBlockID]!!.previousId = newBlock.blockView.id
        newBlock.nextId = endBlockID

        blockList.add(newBlock)
        blockMap[newBlock.blockView.id] = newBlock

        binding.mainWorkfield.addView(newBlock, lastBlock.position + 1)

        newBlock.blockView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        newBlock.position = lastBlock.position + 1
        blockMap[endBlockID]!!.position++


        newBlock.setOnLongClickListener(choiceTouchListener())
        newBlock.setOnDragListener(choiceDragListener())


    }

    private fun View.setDefault(x: Float) {
        this.x = x
        this.z = 1F
        this.id = freeId
        freeId++
        if (freeId == startBlockID || freeId == endBlockID) {
            freeId++
        }
    }

    private fun deleteView(block: CustomView) {
        binding.mainWorkfield.removeView(block.blockView)
        blockMap.remove(block.blockView.id)
        blockList.remove(block)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun choiceTouchListener() = View.OnLongClickListener { view ->
        makeAllEditTextsDisabled()
        val currentBlock = blockMap[view.id]
        if (currentBlock!!.previousId != -1) {
            blockMap[currentBlock.previousId]!!.nextId = currentBlock.nextId
        }
        if (currentBlock.nextId != -1) {
            blockMap[currentBlock.nextId]!!.previousId = currentBlock.previousId
        }

        var block = currentBlock
        while (block!!.blockView.id != endBlockID) {
            block = blockMap[block.nextId]!!
            block.position--
            binding.mainWorkfield.removeView(block.blockView)
        }

        block = currentBlock
        while (block!!.blockView.id != endBlockID) {
            block = blockMap[block.nextId]!!
            binding.mainWorkfield.addView(block.blockView, block.position)
        }
        view.visibility = View.INVISIBLE

        val data = ClipData.newPlainText("", "")
        val shadowBuilder = DragShadowBuilder(blockMap[view.id]!!.blockView)
        view.startDragAndDrop(data, shadowBuilder, view, 0)
        draggingBlock = blockMap[view.id]!!
        true
    }


    private lateinit var draggingBlock: CustomView
    private var location = MyPoint(0f, 0f)

    private fun choiceDragListener() = View.OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                binding.mainWorkfield.invalidate()
                val imm: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(draggingBlock.blockView.windowToken, 0)
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                if (view == binding.mainWorkfield) {
                    location.x = event.x
                    location.y = event.y
                }
            }
            DragEvent.ACTION_DROP -> {
                if (blockMap[view.id] != null || view == binding.start) {
                    if (view.id != endBlockID) {
                        draggingBlock.blockView.z = 1F
                        val currentBlock = draggingBlock
                        val block = view as CustomView

                        val temp = block.nextId
                        block.nextId = currentBlock.blockView.id
                        currentBlock.previousId = block.blockView.id
                        currentBlock.nextId = temp
                        blockMap[temp]!!.previousId = currentBlock.blockView.id

                        currentBlock.position = block.position + 1

                        binding.mainWorkfield.removeView(currentBlock.blockView)
                        binding.mainWorkfield.addView(
                            currentBlock.blockView,
                            currentBlock.position
                        )

                        var counter = 1
                        var tempBlock = currentBlock
                        while (tempBlock.blockView.id != endBlockID) {
                            tempBlock = blockMap[tempBlock.nextId]!!
                            tempBlock.position++
                            counter++
                        }

                        val v = block.blockView
                        val diff = abs(currentBlock.blockView.height - v.height)


                        counter--
                        draggingBlock.blockView.y =
                            v.y + currentBlock.blockView.height * counter + binding.end.height + diff
                        draggingBlock.blockView.x = v.x


                    } else {
                        deleteView(draggingBlock)
                    }
                } else {
                    deleteView(draggingBlock)
                }
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                draggingBlock.blockView.post {
                    draggingBlock.blockView.visibility = View.VISIBLE
                }
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                binding.mainWorkfield.removeView(draggingBlock.blockView)
            }
        }
        true
    }

    private fun makeAllEditTextsDisabled() {
        for (block in blockList) {
            if (block.blockView.id != endBlockID && block.blockView.id != startBlockID)
                block.makeEditTextsDisabled()
        }
    }

    private fun blocksToCode(): String {
        var code = "{\n"
        var currentBlock = blockMap[startBlockID]

        while (currentBlock!!.blockView.id != endBlockID) {
            code += currentBlock.blockToCode() + "\n"
            currentBlock = blockMap[currentBlock.nextId]!!
        }
        code += "}"
        return code
    }


}