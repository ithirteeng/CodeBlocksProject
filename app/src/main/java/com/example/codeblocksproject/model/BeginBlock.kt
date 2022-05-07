package com.example.codeblocksproject.model

import android.view.View

class BeginBlock: CustomView {
    override val isNestingPossible: Boolean
        get() = TODO("Not yet implemented")
    override var previousId: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override var nextId: Int
        get() = TODO("Not yet implemented")
        set(value) {}
    override val blockType: String
        get() = TODO("Not yet implemented")
    override val blockView: View
        get() = TODO("Not yet implemented")
    override val pattern: String
        get() = TODO("Not yet implemented")
    override var position=0

    override fun blockToCode(): String {
        TODO("Not yet implemented")
    }

}