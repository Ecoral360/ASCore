package org.ascore.lang.modules.corekotlin

import org.ascore.lang.modules.EnumModule
import org.ascore.lang.modules.core.ModuleFactory
import org.ascore.lang.modules.core.ModuleManager

enum class EnumModule(moduleFactory: ModuleFactory?) {
    ;

    init {
        ModuleManager.enregistrerModule(EnumModule.valueOf(this.toString()), moduleFactory)
    }
}