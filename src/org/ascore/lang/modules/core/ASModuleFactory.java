package org.ascore.lang.modules.core;

import org.ascore.executor.Executor;

@FunctionalInterface
public interface ASModuleFactory {

    ASModule charger(Executor executorInstance);

}
