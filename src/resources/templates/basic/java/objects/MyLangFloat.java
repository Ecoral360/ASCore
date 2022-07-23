package mylang.objects;

import org.ascore.lang.objects.ASCObject;
import org.ascore.tokens.Token;

/**
 * An example of an object for the MyLang programming main.language
 */
public record MyLangFloat(Double value) implements MyLangNumber {
    public MyLangFloat(Token token) {
        this(Double.parseDouble(token.getValue()));
    }

    @Override
    public Double getValue() {
        return value;
    }
}
