package org.ascore.lang.modules.core;


import org.ascore.executor.ASCExecutor;
import org.ascore.executor.ASCExecutorState;

import java.util.Hashtable;

/**
 * Interface responsable de tous les modules builtins
 *
 * @author Mathis Laroche
 */
public class ModuleManager<ExecutorState extends ASCExecutorState, EnumModule extends Enum<EnumModule>> {
    private final Hashtable<EnumModule, ModuleFactory<ExecutorState>> MODULE_FACTORY = new Hashtable<>();
    private final ASCExecutor<ExecutorState> executorInstance;
    private final Class<EnumModule> enumModuleClass;

    public ModuleManager(ASCExecutor<ExecutorState> executorInstance, Class<EnumModule> enumModuleClass) {
        this.executorInstance = executorInstance;
        this.enumModuleClass = enumModuleClass;
    }

    public void registerModule(EnumModule nomModule, ModuleFactory<ExecutorState> moduleFactory) {
        MODULE_FACTORY.put(nomModule, moduleFactory);
    }

    public void useModule(String nomModule) {
        Module module = getModule(nomModule);
        module.use(nomModule);
    }

    public Module getModule(String nomModule) {
        ModuleFactory<ExecutorState> module;
        module = MODULE_FACTORY.get(Enum.valueOf(enumModuleClass, nomModule));
        return module.load(executorInstance);
    }

    public ASCExecutor<?> executorInstance() {
        return executorInstance;
    }

    @Override
    public String toString() {
        return "ModuleManager[" +
               "executorInstance=" + executorInstance + ']';
    }

}



























