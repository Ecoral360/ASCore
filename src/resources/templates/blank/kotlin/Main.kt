package mylang

import mylang.execution.MyLangExecutorState
import mylang.execution.MyLangPreCompiler
import mylang.lexer.MyLangLexer
import mylang.parser.MyLangParser
import org.ascore.executor.ASCExecutor
import org.ascore.executor.ASCExecutorBuilder

val CODE = """
    """.trimIndent()

fun main() {

    val executor = ASCExecutorBuilder<MyLangExecutorState>() // create an executor builder
        .withLexer(MyLangLexer("/mylang/grammar_rules/Grammar.yaml")) // add the lexer to the builder
        .withParser { executorInstance: ASCExecutor<MyLangExecutorState> ->
            MyLangParser(
                executorInstance
            )
        } // add the parser to the builder
        .withExecutorState(MyLangExecutorState()) // add the executor state to the builder
        .withPrecompiler(MyLangPreCompiler()) // add the precompiler to the builder
        .build() // build the executor

    val compilationResult = executor.compiler(CODE, true) // compile the code

    if (compilationResult.length() != 0) {
        println(compilationResult)
        return
    }
    val executionResult = executor.executerMain(false) // execute the code

    println(executionResult) // print the result

}