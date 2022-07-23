package mylang.lexer

import org.ascore.lang.ASCLexer

/**
 * This class is used to lex the source code. Override and edit the [lex] method to change the
 * lexing behavior.
 */
class MyLangLexer(filePath: String) : ASCLexer(MyLangLexer::class.java.getResourceAsStream(filePath))
