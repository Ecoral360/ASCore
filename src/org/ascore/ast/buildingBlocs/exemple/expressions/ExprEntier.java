package org.ascore.ast.buildingBlocs.exemple.expressions;

import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.lang.objects.datatype.ASEntier;


public record ExprEntier(ASEntier val) implements Expression<ASEntier> {

    @Override
    public ASEntier eval() {
        return val;
    }

    @Override
    public String toString() {
        return "ExprEntier{" +
                "val=" + val +
                '}';
    }
}
