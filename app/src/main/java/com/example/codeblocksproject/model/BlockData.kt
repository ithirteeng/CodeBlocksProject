package com.example.codeblocksproject.model

data class BlockData(
    val type: String,
    val id: Int,
    val nextId: Int,
    val prevId: Int,
    val position: Int,
    val content: ArrayList<String>
)