package org.ascore.executor;

import org.ascore.managers.scope.ASScopeManager;

public class ExecutorState {
    private final ASScopeManager scopeManager = new ASScopeManager();

    public ASScopeManager getScopeManager() {
        return scopeManager;
    }
}
