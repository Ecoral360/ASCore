package utils;


import org.ascore.executor.ASCExecutor;
import org.json.JSONArray;

import static org.junit.Assert.*;

public class ASCoreTester {
    private static ASCExecutor<?> executor = null;
    private static ASCoreExecutionTester executionTester = null;

    public static void resetExecuteur(boolean debug) {
        executor = new ASCExecutor<>();
        executor.debug = debug;
        executionTester = null;
    }

    public static ASCoreExecutionTester assertCompilesAndExecutes(String code) {
        assertCompiles(code);
        return assertExecution();
    }

    public static boolean isDone() {
        return executionTester == null || executionTester.isDone();
    }

    public static void assertCompiles(String code) {
        var result = executor.compiler(code, true);
        assertEquals("[]", result.toString());
    }

    public static ASCoreCompilationTester assertCompilation(String code) {
        var result = executor.compiler(code, true);
        if (executor.debug) System.out.println(result);
        return new ASCoreCompilationTester(result);
    }

    public static ASCoreExecutionTester assertExecution() {
        var aliveScriptExecutionTester = new ASCoreExecutionTester(execute(null));
        executionTester = aliveScriptExecutionTester;
        return aliveScriptExecutionTester;
    }

    public static ASCoreExecutionTester assertExecution(Object responseData) {
        var aliveScriptExecutionTester = new ASCoreExecutionTester(execute(new Object[]{responseData}));
        executionTester = aliveScriptExecutionTester;
        return aliveScriptExecutionTester;
    }

    public static ASCoreExecutionTester assertExecution(Object[] responseData) {
        var aliveScriptExecutionTester = new ASCoreExecutionTester(execute(responseData));
        executionTester = aliveScriptExecutionTester;
        return aliveScriptExecutionTester;
    }

    private static JSONArray execute(Object[] responseData) {
        if (executor.getLignes() == null) {
            throw new IllegalArgumentException("You must compile the lines before executing the code.");
        }
        if (responseData != null) {
            for (var data : responseData)
                executor.pushDataResponse(data);
        }
        var result = executor.executerMain(responseData != null);
        if (executor.debug) System.out.println(result);
        return result;
    }

}































