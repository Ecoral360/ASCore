package org.ascore.lang.modules.core;

import org.ascore.executor.ASCExecutor;
import org.ascore.executor.ASCExecutorState;

@FunctionalInterface
public interface ModuleFactory<T extends ASCExecutorState> {

    Module load(ASCExecutor<T> executorInstance);

}
