package mylang.ast.expressions

import mylang.objects.MyLangFloat
import mylang.objects.MyLangInt
import mylang.objects.MyLangNumber
import mylang.objects.MyLangString
import org.ascore.ast.buildingBlocs.Expression
import org.ascore.lang.objects.ASCObject
import kotlin.math.floor

private operator fun Number.plus(value: Number): Double = this.toDouble() + value.toDouble()
private fun Double.isInt(): Boolean = floor(this) == this

data class AddExpr(private val left: Expression<*>, private val right: Expression<*>) : Expression<ASCObject<*>> {
    override fun eval(): ASCObject<*> {
        val leftValue = left.eval()
        val rightValue = right.eval()
        return when {
            leftValue is MyLangString || rightValue is MyLangString -> {
                MyLangString("" + leftValue.value + rightValue.value)
            }
            leftValue is MyLangNumber && rightValue is MyLangNumber -> {
                val result = leftValue.value!! + rightValue.value!!
                if (result.isInt()) MyLangInt(result.toInt()) else MyLangFloat(result)
            }
            else -> throw ArithmeticException("Addition not supported for '${leftValue.javaClass.simpleName}' and '${rightValue.javaClass.simpleName}'")
        }
    }
}