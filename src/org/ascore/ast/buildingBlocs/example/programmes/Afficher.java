package org.ascore.ast.buildingBlocs.example.programmes;

import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.executor.Executor;
import org.jetbrains.annotations.NotNull;

public class Afficher extends Statement {
    private final Expression<?> exprAfficher;

    public Afficher(Expression<?> expr, @NotNull Executor executor) {
        super(executor);
        this.exprAfficher = expr;
    }

    @Override
    public Object execute() {
        assert executorInstance != null;
        executorInstance.ecrire(exprAfficher.eval().toString());
        return null;
    }
}
