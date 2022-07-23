package mylang.objects

import org.ascore.tokens.Token

/**
 * An example of an object for the MyLang programming main.language
 */
data class MyLangInt(private val value: Int) : MyLangNumber {
    constructor(token: Token) : this(token.value.toInt()) {}

    override fun getValue(): Int = value
}