package mylang.parser

import mylang.ast.expressions.AddExpr
import mylang.ast.expressions.ConstValueExpr
import mylang.ast.statements.PrintStmt
import mylang.execution.MyLangExecutorState
import mylang.objects.MyLangFloat
import mylang.objects.MyLangInt
import mylang.objects.MyLangString
import org.ascore.ast.buildingBlocs.Expression
import org.ascore.executor.ASCExecutor
import org.ascore.generators.ast.AstGenerator
import org.ascore.tokens.Token

/**
 * The parser for the MyLang language.
 *
 * This parser is responsible for defining the rules for parsing the MyLang language. The actual parsing is done by the
 * [AstGenerator] class in accordance with the rules defined in this class.
 *
 *  * Edit the [addExpressions] method to add new expressions to the language.
 *  * Edit the [addStatements] method to add new statements to the language.
 *
 *  @param executorInstance the executor instance to use for executing the AST
 */
class MyLangParser(executorInstance: ASCExecutor<MyLangExecutorState>) : AstGenerator<MyLangAstFrameKind>() {
    private val executorInstance: ASCExecutor<MyLangExecutorState>

    init {
        reset()
        defineAstFrame(MyLangAstFrameKind.DEFAULT)
        addStatements()
        addExpressions()
        pushAstFrame(MyLangAstFrameKind.DEFAULT)
        this.executorInstance = executorInstance
    }

    /**
     * Defines the rules of the statements of the language.
     */
    private fun addStatements() {
        // add your statements here
        addStatement("PRINT expression") { p: List<Any> -> PrintStmt(p[1] as Expression<*>) }
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    private fun addExpressions() {
        // add your expressions here
        addExpression("{datatypes}") { p: List<Any> ->
            val token = p[0] as Token
            when (token.name) {
                "INT" -> ConstValueExpr(MyLangInt(token))
                "FLOAT" -> ConstValueExpr(MyLangFloat(token))
                "STRING" -> ConstValueExpr(MyLangString(token))
                else -> throw NoSuchElementException(token.name)
            }
        }
        addExpression("expression PLUS expression") { p: List<Any> ->
            AddExpr(
                p[0] as Expression<*>, p[2] as Expression<*>
            )
        }
    }
}