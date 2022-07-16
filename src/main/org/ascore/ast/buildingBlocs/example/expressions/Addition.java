package org.ascore.ast.buildingBlocs.example.expressions;

import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.lang.objects.datatype.ASEntier;

public record Addition(ExprEntier nb1, ExprEntier nb2) implements Expression<ASEntier> {
    @Override
    public ASEntier eval() {
        return new ASEntier((nb1.eval()).getValue() + (nb2.eval()).getValue());
    }
}
