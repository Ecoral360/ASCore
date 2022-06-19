package org.ascore.lang.modules.corekotlin

import org.ascore.lang.modules.EnumModule
import org.ascore.lang.modules.core.ASModuleFactory
import org.ascore.lang.modules.core.ASModuleManager

enum class EnumModule(moduleFactory: ASModuleFactory?) {
    ;

    init {
        ASModuleManager.enregistrerModule(EnumModule.valueOf(this.toString()), moduleFactory)
    }
}