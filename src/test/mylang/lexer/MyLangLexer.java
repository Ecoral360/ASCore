package mylang.lexer;

import org.ascore.lang.ASCLexer;

/**
 * This class is used to lex the source code. Override and edit the {@link #lex(String)} method to change the
 * lexing behavior.
 */
public class MyLangLexer extends ASCLexer {
    public MyLangLexer() {
        super(MyLangLexer.class.getResourceAsStream("/Grammar.yaml"));
    }
}
