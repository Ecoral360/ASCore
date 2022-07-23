package mylang.objects

import org.ascore.lang.objects.ASCObject
import org.ascore.tokens.Token

/**
 * An example of an object for the MyLang programming main.language
 */
data class MyLangString(private val value: String) : ASCObject<String> {
    constructor(token: Token) : this(
        token.value.substring(1, token.value.length - 1) // removing the enclosing `"` from the string
    )

    override fun getValue(): String = value
}