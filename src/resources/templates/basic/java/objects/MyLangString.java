package mylang.objects;

import org.ascore.lang.objects.ASCObject;
import org.ascore.tokens.Token;

/**
 * An example of an object for the MyLang programming main.language
 */
public record MyLangString(String value) implements ASCObject<String> {
    public MyLangString(Token token) {
        this(token.getValue().substring(1, token.getValue().length() - 1)); // removing the enclosing `"` from the string
    }

    @Override
    public String getValue() {
        return value;
    }
}
