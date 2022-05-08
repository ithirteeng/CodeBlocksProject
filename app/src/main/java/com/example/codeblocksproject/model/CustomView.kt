package com.example.codeblocksproject.model

import android.view.View

interface CustomView {

    val isNestingPossible: Boolean
    var previousId: Int
    var nextId: Int
    val blockType: String
    val blockView: View
    val pattern: String
    var position: Int
    fun blockToCode(): String
}