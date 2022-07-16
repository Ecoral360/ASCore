package org.ascore.lang.modules.corekotlin

import org.ascore.errors.ASCErrors
import org.ascore.lang.objects.ASParametre
import org.ascore.lang.objects.ASScope
import org.ascore.lang.objects.ASTypeBuiltin
import org.ascore.lang.objects.ASVariable
import org.ascore.lang.objects.datatype.ASNul
import org.ascore.lang.objects.datatype.ASTexte
import org.ascore.managers.data.Data
import org.ascore.executor.ASCExecutor
import org.ascore.executor.ASCExecutorState

object ModuleBuiltin : ASModuleFactory {

    override fun charger(executorInstance: ASCExecutor<*>): ASModule {
        val fonctions = arrayOf(
            fonction(
                "afficher", ASTypeBuiltin.nulType, arrayOf(
                    ASParametre(ASTypeBuiltin.texte, "txt", ASTexte(""))
                )
            ) {
                val txt = it.getValeurParam("txt")
                executorInstance.addData(Data(Data.Id.AVERTISSEMENT).addParam(txt))
                ASNul()
            },
            fonction(
                "getVar", ASTypeBuiltin.tout, arrayOf(
                    ASParametre(ASTypeBuiltin.texte, "nomVariable", null)
                )
            ) {
                val nomVar = it.getValeurParam("nomVariable").value as String
                val variable = ASScope.getCurrentScopeInstance().getVariable(nomVar)
                    ?: throw ASCErrors.ErreurVariableInconnue("La variable '$nomVar' n'est pas d\u00E9clar\u00E9e dans ce scope.")
                variable.valeurApresGetter
            }
        )
        val variables = arrayOf<ASVariable>()

        return ASModule(fonctions)
    }
}










