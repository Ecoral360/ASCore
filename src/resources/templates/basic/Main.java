package mylang;

import mylang.execution.MyLangExecutorState;
import mylang.execution.MyLangPreCompiler;
import mylang.lexer.MyLangLexer;
import mylang.parser.MyLangParser;
import org.ascore.executor.ASCExecutorBuilder;
import org.json.JSONArray;

public class Main {
    private static final String CODE = """
            print 3 + -2
            print "hello" + " " + "world!"
            print "hello " + 3 + 2
            """;

    public static void main(String[] args) {
        var executor = new ASCExecutorBuilder<>()
                .withLexer(new MyLangLexer())
                .withParser(MyLangParser::new)
                .withExecutorState(new MyLangExecutorState())
                .withPrecompiler(new MyLangPreCompiler())
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
