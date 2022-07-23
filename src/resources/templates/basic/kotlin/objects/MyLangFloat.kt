package mylang.objects

import org.ascore.tokens.Token

/**
 * An example of an object for the MyLang programming main.language
 */
data class MyLangFloat(private val value: Double) : MyLangNumber {
    constructor(token: Token) : this(token.value.toDouble()) {}

    override fun getValue(): Double = value

}