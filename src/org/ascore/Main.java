package org.ascore;

import org.json.JSONArray;
import org.ascore.executor.Executor;

public class Main {
    private static final String CODE = """
            """;


    public static void main(String[] args) {
        Executor executor = new Executor();
        JSONArray compilationResult = executor.compiler(CODE.split("\n"), true);
        if (compilationResult.length() != 0) {
            System.out.println(compilationResult);
            return;
        }
        JSONArray executionResult = executor.executerMain(false);
        System.out.println(executionResult);
    }
}
