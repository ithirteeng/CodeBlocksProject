package com.example.codeblocksproject.interpreter

import com.example.codeblocksproject.ConsoleFragment
import java.util.*

val OPERATORS = arrayOf("+", "-", "*", "/", "==", "!=", ">=", ">", "<=", "<", "||", "&&")
val OPERATORS_PRIORITY = mapOf(
    "||" to 1,
    "&&" to 1,
    "==" to 2,
    "!=" to 2,
    ">=" to 2,
    ">" to 2,
    "<=" to 2,
    "<" to 2,
    "+" to 3,
    "-" to 3,
    "*" to 4,
    "/" to 4,
)

class Parser(private val tokens: List<Token>, private val DEBUG: Boolean = false) {
    private var pos: Int = 0
    private val scope = mutableMapOf<String, Any>()

    //    private val scopeArrays = mutableMapOf<String, List<>>()
    private fun match(vararg expected: String): Token? {
        // return com.example.codeblocksproject.interpreter.Token if currentToken is one of expected else null
        if (pos >= tokens.size) {
            println("Match returned null" + "\n vararg was: ${expected[0]}"); throw Error("pos >= tokens.size")
        }

        val currentToken = tokens[pos]
        if (expected.find { type -> tokensMap[type]!!.name == currentToken.type.name } != null) {
            pos++

//            if (DEBUG) {
//                println("Match returned ${currentToken.aboutMe()}")
//            }

            return currentToken
        }

//        if (DEBUG) {
//            println("Match returned null" + "\n vararg was: ${expected[0]}" + " found ${tokens[pos].aboutMe()}")
//        }

        return null
    }

    private fun require(vararg expected: String): Token {
        // return com.example.codeblocksproject.interpreter.Token if currentToken is one of expected else throw exception
        return match(*expected)
            ?: throw Error("Check pos ${tokens[pos].position}. Expected ${tokensMap[expected[0]]?.name}")
    }

    fun run(consoleFragment: ConsoleFragment) {
        while (pos < tokens.size) {
            parsing(consoleFragment)
        }
    }

    private fun parsing(consoleFragment: ConsoleFragment) {
        if (match("var") != null) {
            println("STARTED VAR INITIALIZING PARSING"); parseVarInitializing()
        } else if (match("print") != null) {
            println("STARTED PRINT PARSING"); parsePrint(consoleFragment)
        } else if (match("identifier") != null) {
            println("STARTED ASSIGMENT PARSING"); parseAssignment()
        } else if (match("if") != null) {
            println("STARTED PARSING CONDITIONAL"); parseIf(consoleFragment)
//        } else if (match("while") != null) {
//            println("STARTED LOOP PARSING"); parseLoop()
//        } else if (match("fun") != null) parseFunDeclaration() {

        } else pos++

    }

    private fun parseVarInitializing() {
        val variableName = require("identifier").text
        var isArray = false
        var arrSize = 0
        if (match("[") != null) {
            isArray = true
            arrSize = (parseExpression() as Int)
            require("]")
        }
        require(":")
        val variableType = require("String", "Int").text
        require("=")
        val variableValue = parseExpression()!!
        scope[variableName] =
            if (isArray && arrSize > 0) buildList { for (i in 1..arrSize) add(if (variableType == "Int") 0 else "") } else variableValue
    }

    private fun parsePrint(consoleFragment: ConsoleFragment) {
        val expression = parseExpression()
        if (expression == null) {
            if (DEBUG) {
                consoleFragment.resultsToConsole("Empty output\n")
                println("Empty output")
            }
            println("")
        } else {
            consoleFragment.resultsToConsole("$expression\n")
            println("$expression")
        }
    }

    private fun parseAssignment() {
        val variableName: String = tokens[pos - 1].text
        var arrIndex: Int? = null
        if (match("[") != null) {
            arrIndex = parseExpression() as Int;
            require("]")
        }
        require("=")
        var variableValue = parseExpression()
        if (scope[variableName] != null) {
            if (arrIndex != null) {
                (scope[variableName] as Array<Int>)[arrIndex] = (variableValue as Int)
            } else scope[variableName] = (variableValue as Int)
        } else throw Error("Variable $variableName is not declared!")
    }

    private fun parseExpression(): Any? {
        val operatorsStack = ArrayDeque<String>()
        val numberStack = ArrayDeque<Int>()
        var currentToken = require("number", "identifier", "(")
        var isBooleanOperation: Boolean = false
        while (currentToken.text != ";" && currentToken.text != "{") {
            println(currentToken.aboutMe())
            if (currentToken.text == "(") {
                operatorsStack.push("(")
            } else if (currentToken.text == ")") {
                var currentOperator = operatorsStack.pop()
                while (currentOperator != "(") {
                    val result =
                        calculateExpression(numberStack.pop(), numberStack.pop(), currentOperator)
                    numberStack.push(result)
                    currentOperator = operatorsStack.pop()
                }
            } else if (currentToken.type.group == "operators" || currentToken.type.group == "BooleanOperators") {
                if (operatorsStack.isEmpty() || operatorsStack.last == "(") operatorsStack.push(
                    currentToken.text
                )
                else {
                    val currentOperatorPriority = OPERATORS_PRIORITY[currentToken.text]!!
                    val stackOperatorPriority = OPERATORS_PRIORITY[operatorsStack.last]!!
                    if (currentOperatorPriority > stackOperatorPriority) {
                        operatorsStack.push(currentToken.text)
                    } else {
                        val result = calculateExpression(
                            numberStack.pop(),
                            numberStack.pop(),
                            operatorsStack.pop()
                        )
                        numberStack.push(result)
                        operatorsStack.push(currentToken.text)
                    }
                }
            } else if (currentToken.type.name == "NUMBER") {
                numberStack.push(currentToken.text.toInt())
            } else if (currentToken.type.name == "IDENT_NAME") {
                if (scope[currentToken.text] == null) {
                    throw Error("Check ${currentToken.position}. Variable isn't declared")
                }
                numberStack.push(scope[currentToken.text] as Int)
            }

            currentToken = require("number", "identifier", "[", "]", "(", ")", ";", *OPERATORS, "{")
        }
        while (!operatorsStack.isEmpty()) {
            val result =
                calculateExpression(numberStack.pop(), numberStack.pop(), operatorsStack.pop())
            numberStack.push(result)
        }
        return numberStack.pop()
    }

    private fun calculateExpression(number1: Int, number2: Int, operator: String): Int {
        when (operator) {
            "+" -> {
                return number2 + number1
            }
            "-" -> {
                return number2 - number1
            }
            "*" -> {
                return number2 * number1
            }
            "/" -> {
                return number2 / number1
            }
            "!=" -> {
                return if (number2 != number1) 1 else 0
            }
            "==" -> {
                return if (number2 == number1) 1 else 0
            }
            ">=" -> {
                return if (number2 >= number1) 1 else 0
            }
            "<=" -> {
                return if (number2 <= number1) 1 else 0
            }
            ">" -> {
                return if (number2 > number1) 1 else 0
            }
            "<" -> {
                return if (number2 < number1) 1 else 0
            }
            "||" -> {
                return if ((number1 != 0) || (number2 != 0)) 1 else 0
            }
            "&&" -> {
                return if ((number1 != 0) && (number2 != 0)) 1 else 0
            }
            else -> throw Error("Operator '$operator' isn't supported")
        }
    }

//    private fun parseLoop() {
//        var startConditionPos = pos
//        var isTrue: Boolean = parseExpression() as Int != 0
//        while (isTrue) {
//            parsing()
//            pos = startConditionPos
//            isTrue = parseExpression() as Int != 0
//        }
//
//    }

    private fun parseIf(consoleFragment: ConsoleFragment) {

        val isTrue: Boolean = parseExpression() as Int != 0
        if (isTrue) {
            while (tokens[pos].text != "}") {
                parsing(consoleFragment)
            }
            if (match("else") != null) {
                var openBraces = 1
                var closingBraces = 0
                while (openBraces != closingBraces) {
                    if (match("}") != null) closingBraces++
                    else if (match("{") != null) openBraces++
                    else pos++
                }
            }
        } else {
            var openBraces = 1
            var closingBraces = 0
            while (openBraces != closingBraces) {
                if (match("}") != null) closingBraces++
                else if (match("{") != null) openBraces++
                else pos++
            }
            if (match("else") != null) {
                require("{")
                while (tokens[pos].text != "}") {
                    parsing(consoleFragment)
                }
            }
        }

//        if (isTrue) {
//            var openBraces = 1
//            var closingBraces = 0
//            while (openBraces != closingBraces) {
//                if (match("}") != null) closingBraces++
//                else if (match("{") != null) openBraces++
//                else parsing()
//            }
//        } else {
//            var openBraces = 1
//            var closingBraces = 0
//            while (openBraces != closingBraces) {
//                if (match("}") != null) closingBraces++
//                else if (match("{") != null) openBraces++
//                else pos++
//            }
//            if (match("else") != null) {
//                require("{")
//                var openBraces = 1
//                var closingBraces = 0
//                while (openBraces != closingBraces) {
//                    if (match("}") != null) closingBraces++
//                    else if (match("{") != null) openBraces++
//                    else parsing()
//                }
//            }
//        }
    }
}