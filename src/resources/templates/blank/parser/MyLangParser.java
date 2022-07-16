package mylang.parser;

import mylang.execution.MyLangExecutorState;
import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;

/**
 * The parser for the MyLang language.
 * <br>
 * This parser is responsible for defining the rules for parsing the MyLang language. The actual parsing is done by the
 * {@link AstGenerator} class in accordance with the rules defined in this class.
 * <ul>
 * <li>Edit the {@link #addExpressions()} method to add new expressions to the language.</li>
 * <li>Edit the {@link #addStatements()} method to add new statements to the language.</li>
 * </ul>
 */
public class MyLangParser extends AstGenerator<MyLangAstFrameKind> {
    private ASCExecutor<MyLangExecutorState> executorInstance;

    /**
     * Constructor for the parser.
     *
     * @param executorInstance the executor instance to use for executing the AST
     */
    public MyLangParser(ASCExecutor<MyLangExecutorState> executorInstance) {
        reset(); // reset the parser before starting
        defineAstFrame(MyLangAstFrameKind.DEFAULT); // define the default frame
        addStatements(); // add statements to the language
        addExpressions(); // add expressions to the language
        pushAstFrame(MyLangAstFrameKind.DEFAULT); // push the default frame to the stack so that it is the current frame when parsing starts
        this.executorInstance = executorInstance;
    }

    public ASCExecutor<MyLangExecutorState> getExecutorInstance() {
        return executorInstance;
    }

    /**
     * Sets the executor instance to use for executing the AST. If it was previously set, an exception will be thrown.
     *
     * @param executorInstance the executor instance to use for executing the AST
     * @throws IllegalStateException if the executor instance was already set
     */
    public void setExecutorInstance(ASCExecutor<MyLangExecutorState> executorInstance) {
        if (this.executorInstance != null) { // if the executor instance is already set, throw an exception
            throw new IllegalStateException("executorInstance was already assigned");
        }
        this.executorInstance = executorInstance;
    }

    /**
     * Defines the rules of the statements of the language.
     */
    protected void addStatements() {
        // add your statements here

    }

    /**
     * Defines the rules of the expressions of the language.
     */
    protected void addExpressions() {
        // add your expressions here

    }
}
