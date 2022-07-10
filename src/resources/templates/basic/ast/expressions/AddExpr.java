package mylang.ast.expressions;

import mylang.objects.MyLangFloat;
import mylang.objects.MyLangInt;
import mylang.objects.MyLangNumber;
import mylang.objects.MyLangString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.expressions.Var;
import org.ascore.errors.ASError;
import org.ascore.lang.objects.ASCObject;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te, voir {@link Var}
 *
 * @author Mathis Laroche
 * @see Var
 */
public record AddExpr(Expression<?> left, Expression<?> right) implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        var leftValue = left.eval();
        var rightValue = right.eval();
        if (leftValue instanceof MyLangString || rightValue instanceof MyLangString) {
            return new MyLangString("" + leftValue.getValue() + right.eval().getValue());
        } else if (leftValue instanceof MyLangNumber myLangNumberLeft && rightValue instanceof MyLangNumber myLangNumberRight) {
            var result = myLangNumberLeft.getValue().doubleValue() + myLangNumberRight.getValue().doubleValue();
            return result == (int) result ? new MyLangInt((int) result) : new MyLangFloat(result);
        } else {
            throw new ASError.ErreurArithmetique(
                    "Addition not supported for '" + leftValue.getClass().getSimpleName() +
                    "' and '" + rightValue.getClass().getSimpleName()
            );
        }
    }
}
