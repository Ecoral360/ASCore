package org.ascore.lang;


import org.ascore.generators.lexer.LexerGenerator;
import org.ascore.generators.lexer.LexerLoader;

/**
 * 
 * Les explications vont être rajouté quand j'aurai la motivation de les écrire XD
 * @author Mathis Laroche
 */ 
public class Lexer extends LexerGenerator {
	public Lexer() {
        super();
        LexerLoader loader = new LexerLoader("ascore/regle_et_grammaire/Grammar.yaml");
        loader.load();
        sortTokenRules();
    }
}
