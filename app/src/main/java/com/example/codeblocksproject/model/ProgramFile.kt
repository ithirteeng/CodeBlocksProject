package com.example.codeblocksproject.model

import android.content.Context
import com.example.codeblocksproject.WorkspaceFragment
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ProgramFile(val context: Context) {

    private fun blockToData(block: CustomView): BlockData {
        return BlockData(
            block.blockType,
            block.blockView.id,
            block.nextId,
            block.previousId,
            block.position,
            block.content()
        )
    }

    private fun blockMapToJson(
        blockMap: Map<Int, CustomView>,
        startBlockID: Int,
        endBlockID: Int
    ): String {
        val array: ArrayList<BlockData> = arrayListOf()

        var block = blockMap[startBlockID]!!
        while (block.nextId != endBlockID) {
            block = blockMap[block.nextId]!!
            array.add(blockToData(block))
        }

        return Gson().toJson(array)
    }

    fun saveProgram(
        blockMap: Map<Int, CustomView>,
        startBlockID: Int,
        endBlockID: Int,
        fileName: String = WorkspaceFragment.FILE_NAME
    ) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(blockMapToJson(blockMap, startBlockID, endBlockID).toByteArray())
            it.close()
        }

    }

    fun loadProgram(fileName: String = WorkspaceFragment.FILE_NAME): Map<Int, CustomView> {
        val json = context.openFileInput(fileName).bufferedReader().use {
            it.readText()
        }
        return dataToMap(Json.decodeFromString(json))
    }

    private fun dataToMap(list: List<BlockData>): MutableMap<Int, CustomView> {
        val blockMap: MutableMap<Int, CustomView> = mutableMapOf()

        for (blockData in list) {
            val newBlock: CustomView
            when (blockData.type) {
                BlockTypes.INIT_BLOCK_TYPE -> {
                    newBlock = InitializationBlock(context)
                }
                BlockTypes.ASSIGN_BLOCK_TYPE -> {
                    newBlock = AssignmentBlock(context)
                }
                BlockTypes.OUTPUT_BLOCK_TYPE -> {
                    newBlock = OutputBlock(context)
                }
                BlockTypes.WHILE_BLOCK_TYPE -> {
                    newBlock = WhileBlock(context)
                }
                BlockTypes.IF_BLOCK_TYPE -> {
                    newBlock = IfBlock(context)
                }
                BlockTypes.ELSE_BLOCK_TYPE -> {
                    newBlock = ElseBlock(context)
                }
                BlockTypes.BEGIN_BLOCK_TYPE -> {
                    newBlock = BeginBlock(context)
                }
                BlockTypes.ARRAY_INIT_BLOCK_TYPE -> {
                    newBlock = ArrayInitBlock(context)
                }
                BlockTypes.INPUT_BLOCK_TYPE -> {
                    newBlock = InputBlock(context)
                }
                else -> {
                    newBlock = EndBlock(context)
                }
            }

            newBlock.loadBlock(blockData)
            newBlock.blockView.z = 1F
            blockMap[newBlock.blockView.id] = newBlock
        }

        return blockMap
    }

}