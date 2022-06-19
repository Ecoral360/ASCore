package org.ascore.lang.modules.core;

import org.ascore.executor.Executor;

@FunctionalInterface
public interface ModuleFactory {

    Module load(Executor executorInstance);

}
