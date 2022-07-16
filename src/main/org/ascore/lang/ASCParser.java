package org.ascore.lang;

import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;


/**
 * Classe o\u00F9 sont d\u00E9finis les statements et les expressions support\u00E9s par le
 * langage
 *
 * @author Mathis Laroche
 */
public class ASCParser extends AstGenerator<ParserFrameKind> {
    private final ASCExecutor executorInstance;

    public ASCParser(ASCExecutor executorInstance) {
        reset();
        defineAstFrame(ParserFrameKind.DEFAULT);
        addStatements();
        addExpressions();
        pushAstFrame(ParserFrameKind.DEFAULT);
        this.executorInstance = executorInstance;
    }

    protected void addStatements() {
        // add your statements here

    }

    protected void addExpressions() {
        // add your expressions here

    }
}

























