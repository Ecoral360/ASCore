package org.ascore.lang.modules;

import org.ascore.lang.modules.builtins.ModuleBuiltin;
import org.ascore.lang.modules.core.ModuleFactory;
import org.ascore.lang.modules.core.ModuleManager;

public enum EnumModule {
    builtins(ModuleBuiltin::load),
    ;

    EnumModule(ModuleFactory moduleFactory) {
        ModuleManager.enregistrerModule(this, moduleFactory);
    }
}
