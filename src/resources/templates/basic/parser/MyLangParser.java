package mylang.parser;

import mylang.ast.expressions.AddExpr;
import mylang.ast.expressions.ConstValueExpr;
import mylang.ast.statements.PrintStmt;
import mylang.objects.MyLangFloat;
import mylang.objects.MyLangInt;
import mylang.objects.MyLangString;
import mylang.execution.MyLangExecutorState;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;
import org.ascore.tokens.Token;

import java.util.NoSuchElementException;

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
        reset();
        defineAstFrame(MyLangAstFrameKind.DEFAULT);
        addStatements();
        addExpressions();
        pushAstFrame(MyLangAstFrameKind.DEFAULT);
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
        if (this.executorInstance != null) {
            throw new IllegalStateException("executorInstance was already assigned");
        }
        this.executorInstance = executorInstance;
    }

    /**
     * Defines the rules of the statements of the language.
     */
    protected void addStatements() {
        // add your statements here
        addStatement("PRINT expression", p -> new PrintStmt((Expression<?>) p.get(1)));
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    protected void addExpressions() {
        // add your expressions here
        addExpression("{datatypes}", p -> {
            var token = (Token) p.get(0);
            return switch (token.getName()) {
                case "INT" -> new ConstValueExpr(new MyLangInt(token));
                case "FLOAT" -> new ConstValueExpr(new MyLangFloat(token));
                case "STRING" -> new ConstValueExpr(new MyLangString(token));
                default -> throw new NoSuchElementException(token.getName());
            };
        });

        addExpression("expression PLUS expression", p -> new AddExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2)));

    }
}
