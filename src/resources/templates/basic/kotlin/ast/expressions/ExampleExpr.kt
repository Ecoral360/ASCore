package mylang.ast.expressions

import org.ascore.ast.buildingBlocs.Expression
import org.ascore.lang.objects.ASCObject
import org.ascore.lang.objects.datatype.ASNul

/**
 * Example of the implementation of an expression.
 */
class ExampleExpr : Expression<ASCObject<*>> {
    /**
     * Often called at runtime, this method returns an ASCObject
     *
     * @return the result of the expression once evaluated
     */
    override fun eval(): ASCObject<*> {
        return ASNul()
    }
}