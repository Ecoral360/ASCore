package mylang.parser;

import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;

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

    }

    protected void addExpressions() {
        // add your expressions here

    }
}
