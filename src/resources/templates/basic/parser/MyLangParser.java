package mylang.parser;

import mylang.ast.expressions.AddExpr;
import mylang.ast.expressions.ConstValueExpr;
import mylang.ast.statements.PrintStmt;
import mylang.objects.MyLangFloat;
import mylang.objects.MyLangInt;
import mylang.objects.MyLangString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;
import org.ascore.tokens.Token;

import java.util.NoSuchElementException;

public class MyLangParser extends AstGenerator<MyLangAstFrameKind> {
    private ASCExecutor executorInstance;

    public MyLangParser(ASCExecutor executorInstance) {
        reset();
        defineAstFrame(MyLangAstFrameKind.DEFAULT);
        addStatements();
        addExpressions();
        pushAstFrame(MyLangAstFrameKind.DEFAULT);
        this.executorInstance = executorInstance;
    }

    public ASCExecutor getExecutorInstance() {
        return executorInstance;
    }

    public void setExecutorInstance(ASCExecutor executorInstance) {
        if (this.executorInstance != null) {
            throw new IllegalStateException("executorInstance was already assigned");
        }
        this.executorInstance = executorInstance;
    }

    protected void addStatements() {
        // add your statements here
        addStatement("PRINT expression", p -> new PrintStmt((Expression<?>) p.get(1)));
    }

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
