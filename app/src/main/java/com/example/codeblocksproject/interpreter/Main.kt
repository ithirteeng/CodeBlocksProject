package com.example.codeblocksproject.interpreter

import Lexer
import Parser

fun main(code: String) {
    val lexer = Lexer(code, DEBUG = true)
    val tokens = lexer.lexicalAnalysis()

    tokens.forEach { x -> println(x.aboutMe()) }
    val parser = Parser(tokens, DEBUG = true)
    parser.run()
//    val parser = Parser(tokens, true)
//    val rootNode = parser.parseCode()
//    println("\n ${((rootNode as StatementsNode).codeStrings[0] as BinaryOperatorNode).rightNode}")
//    parser.run(rootNode)
}

//    val regex = Regex("^" + tokenTypeMap["NUMBER"]?.regex?.pattern)
// Try adding program arguments via Run/Debug configuration.
// Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
