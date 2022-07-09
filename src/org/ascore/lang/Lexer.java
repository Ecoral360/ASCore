package org.ascore.lang;


import org.ascore.generators.lexer.LexerGenerator;
import org.ascore.generators.lexer.LexerLoader;

/**
 * 
 * Les explications vont être rajouté quand j'aurai la motivation de les écrire XD
 * @author Mathis Laroche
 */ 
public class Lexer extends LexerGenerator {
    private final LexerLoader loader;

    public Lexer() {
        this("ascore/grammar_rules/Grammar.yaml");
    }

    public Lexer(String fileName) {
        this.loader = new LexerLoader(fileName);
        this.loader.load();
        this.sortTokenRules();
    }

    public LexerLoader getLoader() {
        return this.loader;
    }
}
