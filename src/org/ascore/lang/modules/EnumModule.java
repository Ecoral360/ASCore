package org.ascore.lang.modules;

import org.ascore.lang.modules.builtins.ModuleBuiltin;
import org.ascore.lang.modules.core.ASModuleFactory;
import org.ascore.lang.modules.core.ASModuleManager;

public enum EnumModule {
    builtins(ModuleBuiltin::charger),
    ;

    EnumModule(ASModuleFactory moduleFactory) {
        ASModuleManager.enregistrerModule(this, moduleFactory);
    }
}
