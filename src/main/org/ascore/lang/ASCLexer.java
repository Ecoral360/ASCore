package org.ascore.lang;


import org.ascore.generators.lexer.LexerGenerator;
import org.ascore.generators.lexer.LexerLoader;

import java.io.InputStream;

/**
 * 
 * Les explications vont être rajouté quand j'aurai la motivation de les écrire XD
 * @author Mathis Laroche
 */ 
public class ASCLexer extends LexerGenerator {
    private final LexerLoader loader;

    public ASCLexer() {
        this("ascore/grammar_rules/Grammar.yaml");
    }

    public ASCLexer(String fileName) {
        this.loader = new LexerLoader(fileName);
        this.loader.load();
        this.sortTokenRules();
    }

    public ASCLexer(InputStream inputStream) {
        this.loader = new LexerLoader(inputStream);
        this.loader.load();
        this.sortTokenRules();
    }

    public LexerLoader getLoader() {
        return this.loader;
    }
}
