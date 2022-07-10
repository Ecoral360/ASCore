package mylang.ast.expressions;

import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.expressions.Var;
import org.ascore.lang.objects.ASCObject;
import org.ascore.lang.objects.datatype.ASNul;

/**
 * Example of the implementation of an expression.
 */
public class ExampleExpr implements Expression<ASCObject<?>> {

    /**
     * Often called at runtime, this method returns an ASCObject<?>
     *
     * @return the result of the expression once evaluated
     */
    @Override
    public ASCObject<?> eval() {
        return new ASNul();
    }
}
