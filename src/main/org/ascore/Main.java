package org.ascore;

import org.ascore.executor.ASCExecutorBuilder;
import org.ascore.executor.ASCExecutorState;
import org.ascore.executor.PreCompiler;
import org.ascore.lang.ASCLexer;
import org.ascore.lang.ASCParser;
import org.json.JSONArray;

public class Main {
    private static final String CODE = """
            """;


    public static void main(String[] args) {
        var executor = new ASCExecutorBuilder<>()
                .withLexer(new ASCLexer("/grammar_rules/Grammar.yaml"))
                .withParser(ASCParser::new)
                .withExecutorState(new ASCExecutorState())
                .withPrecompiler(new PreCompiler())
                .build();
        JSONArray compilationResult = executor.compiler(CODE.split("\n"), true);
        if (compilationResult.length() != 0) {
            System.out.println(compilationResult);
            return;
        }
        JSONArray executionResult = executor.executerMain(false);
        System.out.println(executionResult);
    }
}
