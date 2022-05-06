package com.example.codeblocksproject.model

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

interface CustomView {
    val isNestingPossible: Boolean
    var previousId: Int
    var nextId: Int
    val blockType: String
    val blockView:View
    val pattern:String
    fun blockToCode():String
}