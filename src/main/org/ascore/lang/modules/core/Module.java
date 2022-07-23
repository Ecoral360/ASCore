package org.ascore.lang.modules.core;

import org.ascore.lang.objects.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe repr\u00E9sentant un module.<br>
 * Un module est un ensemble de {@link ASFonctionModule fonctions} et de
 * {@link ASVariable variables}/{@link ASConstante constantes}
 * qui, lorsqu'{@link #use(String) utiliser}, sont d\u00E9clar\u00E9es dans le scope
 * pour \u00EAtre utilis\u00E9 plus loin dans le code
 *
 * @author Mathis Laroche
 */
public record Module(ASFonctionModule[] fonctionModules,
                     ASVariable[] variables) {

    public Module(ASFonctionModule[] fonctionModules) {
        this(fonctionModules, new ASVariable[]{});
    }

    public Module(ASVariable[] variables) {
        this(new ASFonctionModule[]{}, variables);
    }

    public void use(String prefix) {
        //ASFonctionManager.ajouterStructure(prefix);
        for (ASFonctionModule fonctionModule : fonctionModules) {
            ASScope.getCurrentScope().declareVariable(new ASVariable(fonctionModule.getNom(), fonctionModule, new ASType(fonctionModule.obtenirNomType())));
        }
        for (ASVariable variable : variables) {
            ASScope.getCurrentScope().declareVariable(variable.clone());
        }
        //ASFonctionManager.retirerStructure();
    }

    public void use(List<String> nomMethodes) {
        for (ASFonctionModule fonctionModule : fonctionModules) {
            if (nomMethodes.contains(fonctionModule.getNom())) {
                // ASFonctionManager.ajouterFonction(fonctionModule);
                ASScope.getCurrentScope().declareVariable(new ASVariable(fonctionModule.getNom(), fonctionModule, new ASType(fonctionModule.obtenirNomType())));
            }
        }
        for (ASVariable variable : variables) {
            if (nomMethodes.contains(variable.obtenirNom())) {
                ASScope.getCurrentScope().declareVariable(variable);
            }
        }
    }

    /**
     * @return un array contenant toutes les fonctions du module
     */
    public ASFonctionModule[] getFonctions() {
        return fonctionModules;
    }

    /**
     * @return un array contenant toutes les variables du module
     */
    public ASVariable[] getVariables() {
        return variables;
    }

    /**
     * @return la liste des noms des fonctions du module
     */
    public List<String> getNomsFonctions() {
        if (fonctionModules.length == 0) return new ArrayList<>();
        return Stream.of(fonctionModules).map(ASFonctionModule::getNom).collect(Collectors.toList());
    }

    /**
     * @return la liste des noms des constantes du module
     */
    public List<String> getNomsVariables() {
        if (variables.length == 0) return new ArrayList<>();
        return Stream.of(variables).map(ASVariable::obtenirNom).collect(Collectors.toList());
    }

    /**
     * @return la liste des noms des constantes du module
     */
    public List<String> getNomsConstantesEtFonctions() {
        List<String> noms = getNomsFonctions();
        noms.addAll(getNomsVariables());
        return noms;
    }

    @Override
    public String toString() {
        return "Module{\n" +
                "fonctions=" + Arrays.toString(fonctionModules) + "\n" +
                ", variables=" + Arrays.toString(variables) + "\n" +
                '}';
    }
}














