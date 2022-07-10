package mylang.lexer;

import org.ascore.lang.ASCLexer;

public class MyLangLexer extends ASCLexer {
    public MyLangLexer() {
        super(MyLangLexer.class.getResourceAsStream("/grammar_rules/Grammar.yaml"));
    }
}
