package org.ascore.lang.modules.core;

import org.ascore.executor.ASCExecutor;

@FunctionalInterface
public interface ModuleFactory {

    Module load(ASCExecutor executorInstance);

}
