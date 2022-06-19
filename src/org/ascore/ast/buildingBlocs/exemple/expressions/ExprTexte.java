package org.ascore.ast.buildingBlocs.exemple.expressions;

import org.ascore.lang.objects.datatype.ASTexte;
import org.ascore.ast.buildingBlocs.Expression;

public record ExprTexte(ASTexte val) implements Expression<ASTexte> {

    @Override
    public ASTexte eval() {
        return val;
    }

    @Override
    public String toString() {
        return "ExprTexte{" +
                "val=" + val +
                '}';
    }
}
