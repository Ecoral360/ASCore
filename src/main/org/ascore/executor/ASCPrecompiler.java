package org.ascore.executor;

import org.jetbrains.annotations.Contract;

public abstract class ASCPrecompiler {
    @Contract(pure = true)
    public abstract String preCompile(String program);
}
