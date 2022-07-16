package org.ascore.lang.modules.corekotlin

import org.ascore.lang.objects.ASFonctionModule
import org.ascore.lang.objects.ASParametre
import org.ascore.lang.objects.ASTypeBuiltin
import org.ascore.lang.objects.ASObjet
import org.ascore.executor.ASCExecutor

interface ASModuleFactory {
    fun charger(executorInstance: ASCExecutor<*>): ASModule

    fun fonction(
        nom: String,
        typeRetour: ASTypeBuiltin,
        parametres: Array<ASParametre>,
        effet: (ASFonctionModule) -> ASObjet<*>
    ): ASFonctionModule {
        return object : ASFonctionModule(nom, typeRetour, parametres) {
            override fun executer(): ASObjet<*> {
                return effet(this)
            }
        }
    }

    fun fonction(
        nom: String,
        typeRetour: ASTypeBuiltin,
        effet: (ASFonctionModule) -> ASObjet<*>
    ): ASFonctionModule {
        return object : ASFonctionModule(nom, typeRetour, arrayOf()) {
            override fun executer(): ASObjet<*> {
                return effet(this)
            }
        }
    }
}