package org.ascore.executor;

import org.ascore.generators.ast.AstGenerator;
import org.ascore.lang.ASCLexer;
import org.ascore.lang.ASCParser;
import org.ascore.lang.modules.core.ModuleManager;

import java.util.function.Function;

public class ASCExecutorBuilder {
    private ASCLexer lexer;
    private AstGenerator<?> parser;
    private Function<ASCExecutor, ? extends AstGenerator<?>> parserGenerator;
    private ASCExecutorState executorState;
    private ASCPrecompiler precompiler;
    private ModuleManager moduleManager;


    public ASCExecutorBuilder withLexer(ASCLexer lexer) {
        this.lexer = lexer;
        return this;
    }

    public ASCExecutorBuilder withParser(ASCParser parser) {
        if (parserGenerator != null) {
            throw new IllegalStateException("the parser was already defined (using `withParser(Function<ASCExecutor, ASCParser> parserGenerator)` )");
        }
        this.parser = parser;
        return this;
    }

    public ASCExecutorBuilder withParser(Function<ASCExecutor, ? extends AstGenerator<?>> parserGenerator) {
        if (parser != null) {
            throw new IllegalStateException("the parser was already defined (using `withParser(ASCParser parser)` )");
        }
        this.parserGenerator = parserGenerator;
        return this;
    }

    public ASCExecutorBuilder withExecutorState(ASCExecutorState executorState) {
        this.executorState = executorState;
        return this;
    }

    public ASCExecutorBuilder withPrecompiler(ASCPrecompiler precompiler) {
        this.precompiler = precompiler;
        return this;
    }

    public ASCExecutor build() {
        var executor = new ASCExecutor(lexer, parser, null, precompiler, executorState);
        if (parser == null && parserGenerator != null) {
            executor.setParser(parserGenerator.apply(executor));
        }
        return executor;
    }
}
