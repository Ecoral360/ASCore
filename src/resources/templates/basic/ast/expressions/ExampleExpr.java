package mylang.ast.expressions;

import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.expressions.Var;
import org.ascore.lang.objects.ASCObject;
import org.ascore.lang.objects.datatype.ASNul;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te, voir {@link Var}
 *
 * @author Mathis Laroche
 * @see Var
 */
public class ExampleExpr implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        return new ASNul();
    }
}
