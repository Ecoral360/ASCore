package mylang.parser

import mylang.execution.MyLangExecutorState
import org.ascore.executor.ASCExecutor
import org.ascore.generators.ast.AstGenerator

/**
 * The parser for the MyLang language.
 * <br></br>
 * This parser is responsible for defining the rules for parsing the MyLang language. The actual parsing is done by the
 * [AstGenerator] class in accordance with the rules defined in this class.
 *
 *  * Edit the [addExpressions] method to add new expressions to the language.
 *  * Edit the [addStatements] method to add new statements to the language.
 *
 *  @param executorInstance the executor instance to use for executing the AST
 *
 */
class MyLangParser(val executorInstance: ASCExecutor<MyLangExecutorState>) : AstGenerator<MyLangAstFrameKind?>() {

    /**
     * Constructor for the parser.
     *
     *
     */
    init {
        reset() // reset the parser before starting
        defineAstFrame(MyLangAstFrameKind.DEFAULT) // define the default frame
        addStatements() // add statements to the language
        addExpressions() // add expressions to the language
        pushAstFrame(MyLangAstFrameKind.DEFAULT) // push the default frame to the stack so that it is the current frame when parsing starts
    }

    /**
     * Defines the rules of the statements of the language.
     */
    private fun addStatements() {
        // add your statements here
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    private fun addExpressions() {
        // add your expressions here
    }
}