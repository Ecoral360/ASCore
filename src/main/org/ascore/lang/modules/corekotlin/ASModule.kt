package org.ascore.lang.modules.corekotlin

import org.ascore.lang.objects.ASFonctionModule
import org.ascore.lang.objects.ASScope
import org.ascore.lang.objects.ASType
import org.ascore.lang.objects.ASVariable
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Classe repr\u00E9sentant un module.<br></br>
 * Un module est un ensemble de [fonctions][ASFonctionModule] et de
 * [variables][ASVariable]/[constantes][ASConstante]
 * qui, lorsqu'[utiliser][.utiliser], sont d\u00E9clar\u00E9es dans le scope
 * pour \u00EAtre utilis\u00E9 plus loin dans le code
 *
 * @author Mathis Laroche
 */
class ASModule(
    private val fonctionModules: Array<ASFonctionModule> = arrayOf(),
    private val variables: Array<ASVariable> = arrayOf()
) {

    fun utiliser(prefix: String) {
        for (fonctionModule in fonctionModules) {
            ASScope.getCurrentScope().declareVariable(
                ASVariable(
                    fonctionModule.nom,
                    fonctionModule,
                    ASType(fonctionModule.obtenirNomType())
                )
            )
        }
        for (variable in variables) {
            ASScope.getCurrentScope().declareVariable(variable.clone())
        }
    }

    fun utiliser(nomMethodes: List<String>) {
        for (fonctionModule in fonctionModules) {
            if (nomMethodes.contains(fonctionModule.nom))
            // ASFonctionManager.ajouterFonction(fonctionModule);
                ASScope.getCurrentScope().declareVariable(
                    ASVariable(
                        fonctionModule.nom,
                        fonctionModule,
                        ASType(fonctionModule.obtenirNomType())
                    )
                )
        }
        for (variable in variables) {
            if (nomMethodes.contains(variable.obtenirNom())) {
                ASScope.getCurrentScope().declareVariable(variable)
            }
        }
    }

    /**
     * @return la liste des noms des fonctions du module
     */
    val nomsFonctions: MutableList<String>
        get() = if (fonctionModules.isEmpty()) ArrayList() else Stream.of(*fonctionModules)
            .map { obj: ASFonctionModule -> obj.nom }
            .toList()

    /**
     * @return la liste des noms des constantes du module
     */
    val nomsVariables: MutableList<String>
        get() = if (variables.isEmpty()) ArrayList() else Stream.of(*variables)
            .map { obj: ASVariable -> obj.obtenirNom() }
            .collect(Collectors.toList())

    /**
     * @return la liste des noms des constantes du module
     */
    val nomsConstantesEtFonctions: List<String>
        get() {
            val noms = nomsFonctions
            noms.addAll(nomsVariables)
            return noms
        }

    override fun toString(): String {
        return """
             Module{
             fonctions=${fonctionModules.contentToString()}
             , variables=${variables.contentToString()}
             }
             """.trimIndent()
    }
}