package com.example.codeblocksproject.interpreter

class TokenType(val name: String, val group: String = "default", val regex: Regex)
class Token(
    val type: TokenType,
    val text: String,
    val stringNumber: Int,
    val stringPos: Int,
    val position: Int
) {
    fun aboutMe(): String {
        return "com.example.codeblocksproject.interpreter.Token(${type.name}, '$text', string $stringNumber, stringPos ${stringPos}, pos: $position)"
    }

}

val tokensMap: Map<String, TokenType>
    get() = mapOf(
        "SPACE" to TokenType("SPACE", "spaces", Regex("[ \\t\\r]")),
        "\\n" to TokenType("NEWSTR", "spaces", Regex("\\n")),
        "input" to TokenType("INPUT", "ReservedWords", Regex("input")),
        "print" to TokenType("PRINT", "ReservedWords", Regex("print")),
        "if" to TokenType("IF", "ReservedWords", Regex("if")),
        "else" to TokenType("ELSE", "ReservedWords", Regex("else")),
        "EOF" to TokenType("EOF", "ReservedWords", Regex("EOF")),
        "while" to TokenType("WHILE", "ReservedWords", Regex("while")),
        "return" to TokenType("RETURN", "ReservedWords", Regex("return")),
        "String" to TokenType("TypeString", "ReservedWords", Regex("String")),
        "Int" to TokenType("TypeInt", "ReservedWords", Regex("Int")),
        "Array" to TokenType("TypeArray", "ReservedWords", Regex("Array")),
        "var" to TokenType("VAR", "ReservedWords", Regex("var")),
        "fun" to TokenType("FUN", "ReservedWords", Regex("fun")),
        "stringValue" to TokenType("STRING", "Types", Regex("\"[\\w\\s]*\"")),
        "identifier" to TokenType("IDENT_NAME", "Identifiers", Regex("[A-Za-z_]\\w*")),
        "comment" to TokenType("COMMENT", "GrammarSigns", Regex("!!!([^!]|!{1,2})*?!!!")),
        ";" to TokenType("SEMICOLON", "GrammarSigns", Regex(";")),
        "," to TokenType("COMMA", "GrammarSigns", Regex(",")),
        ":" to TokenType("COLON", "GrammarSigns", Regex(":")),
        "(" to TokenType("LBRACKET", "Braces", Regex("\\(")),
        ")" to TokenType("RBRACKET", "Braces", Regex("\\)")),
        "[" to TokenType("SqLBRACKET", "Braces", Regex("\\[")),
        "]" to TokenType("SqRBRACKET", "Braces", Regex("]")),
        "{" to TokenType("CuLBRACKET", "Braces", Regex("\\{")),
        "}" to TokenType("CuRBRACKET", "Braces", Regex("\\}")),
        "!=" to TokenType("UNEQUALLY", "BooleanOperators", Regex("!=")),
        "==" to TokenType("EQUALS", "BooleanOperators", Regex("==")),
        "<=" to TokenType("LESS|EQUALS", "BooleanOperators", Regex("<=")),
        ">=" to TokenType("GREATER|EQUALS", "BooleanOperators", Regex(">=")),
        "+=" to TokenType("PLUS_ASSIGN", "AssignOperators", Regex("\\+=")),
        "-=" to TokenType("MINUS_ASSIGN", "AssignOperators", Regex("-=")),
        "+" to TokenType("PLUS", "operators", Regex("\\+")),
        "-" to TokenType("MINUS", "operators", Regex("-")),
        "/" to TokenType("DIVIDE", "operators", Regex("/")),
        "%" to TokenType("MOD", "operators", Regex("%")),
        "*" to TokenType("MULTIPLY", "operators", Regex("\\*")),
        "||" to TokenType("OR", "BooleanOperators", Regex("\\|\\|")),
        "&&" to TokenType("AND", "BooleanOperators", Regex("&&")),

        "<" to TokenType("LESS", "BooleanOperators", Regex("<")),
        ">" to TokenType("GREATER", "BooleanOperators", Regex(">")),
        "=" to TokenType("ASSIGN", "AssignOperators", Regex("=")),
        "number" to TokenType("NUMBER", "Types", Regex("(0|-?[1-9]\\d*)")),
    )