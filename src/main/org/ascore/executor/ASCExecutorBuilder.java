package org.ascore.executor;

import org.ascore.generators.ast.AstGenerator;
import org.ascore.lang.ASCLexer;
import org.ascore.lang.ASCParser;

import java.util.function.Function;

public class ASCExecutorBuilder<ExecutorState extends ASCExecutorState> {
    private ASCLexer lexer;
    private AstGenerator<?> parser;
    private Function<ASCExecutor<ExecutorState>, ? extends AstGenerator<?>> parserGenerator;
    private ExecutorState executorState;
    private ASCPrecompiler precompiler;


    public ASCExecutorBuilder<ExecutorState> withLexer(ASCLexer lexer) {
        this.lexer = lexer;
        return this;
    }

    public ASCExecutorBuilder<ExecutorState> withParser(ASCParser parser) {
        if (parserGenerator != null) {
            throw new IllegalStateException("the parser was already defined (using `withParser(Function<ASCExecutor, ASCParser> parserGenerator)` )");
        }
        this.parser = parser;
        return this;
    }

    public ASCExecutorBuilder<ExecutorState> withParser(Function<ASCExecutor<ExecutorState>, ? extends AstGenerator<?>> parserGenerator) {
        if (parser != null) {
            throw new IllegalStateException("the parser was already defined (using `withParser(ASCParser parser)` )");
        }
        this.parserGenerator = parserGenerator;
        return this;
    }

    public ASCExecutorBuilder<ExecutorState> withExecutorState(ExecutorState executorState) {
        this.executorState = executorState;
        return this;
    }

    public ASCExecutorBuilder<ExecutorState> withPrecompiler(ASCPrecompiler precompiler) {
        this.precompiler = precompiler;
        return this;
    }

    public ASCExecutor<ExecutorState> build() {
        var executor = new ASCExecutor<>(lexer, parser, precompiler, executorState);
        if (parser == null && parserGenerator != null) {
            executor.setParser(parserGenerator.apply(executor));
        }
        return executor;
    }
}
