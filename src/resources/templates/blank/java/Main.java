package mylang;

import mylang.execution.MyLangExecutorState;
import mylang.execution.MyLangPreCompiler;
import mylang.lexer.MyLangLexer;
import mylang.parser.MyLangParser;
import org.ascore.executor.ASCExecutorBuilder;
import org.json.JSONArray;

/**
 * Entry point for the MyLang language where you can try it out and experiment with it while developing it.
 */
public class Main {
    /**
     * The CODE lines to execute.
     */
    private static final String CODE = """
            """;

    public static void main(String[] args) {
        var executor = new ASCExecutorBuilder<>() // create an executor builder
                .withLexer(new MyLangLexer("/Grammar.yaml")) // add the lexer to the builder
                .withParser(MyLangParser::new) // add the parser to the builder
                .withExecutorState(new MyLangExecutorState()) // add the executor state to the builder
                .withPrecompiler(new MyLangPreCompiler()) // add the precompiler to the builder
                .build(); // build the executor
        JSONArray compilationResult = executor.compiler(CODE, true); // compile the code
        if (compilationResult.length() != 0) {
            System.out.println(compilationResult);
            return;
        }
        JSONArray executionResult = executor.executerMain(false); // execute the code
        System.out.println(executionResult); // print the result
    }
}
