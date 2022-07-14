package mylang.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the MyLang programming main.language
 */
public record MyLangInt(Integer value) implements MyLangNumber {
    public MyLangInt(Token token) {
        this(Integer.parseInt(token.getValue()));
    }

    @Override
    public Integer getValue() {
        return value;
    }
}