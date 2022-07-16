package org.ascore.lang.modules.core;


import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.lang.objects.ASConstante;
import org.ascore.lang.objects.ASScope;
import org.ascore.lang.objects.datatype.ASListe;
import org.ascore.lang.objects.datatype.ASTexte;
import org.ascore.lang.modules.EnumModule;

import java.util.*;

/**
 * Interface responsable de tous les modules builtins
 *
 * @author Mathis Laroche
 */
public record ModuleManager(ASCExecutor executorInstance) {
    private final static Hashtable<EnumModule, ModuleFactory> MODULE_FACTORY = new Hashtable<>();
    /*
    TABLE DES MATIERES:
    Module:
    -builtins

    -Voiture
    -Math
     */

    public static void enregistrerModule(EnumModule nomModule, ModuleFactory moduleFactory) {
        MODULE_FACTORY.put(nomModule, moduleFactory);
    }

    public Module getModuleBuiltins() {
        return MODULE_FACTORY.get(EnumModule.builtins).load(executorInstance);
    }

    public void utiliserModuleBuitlins() {
        var moduleBuiltins = getModuleBuiltins();
        moduleBuiltins.utiliser((String) null);
        ASScope.getCurrentScope().declarerVariable(new ASConstante("builtins", new ASListe(moduleBuiltins
                .getNomsConstantesEtFonctions()
                .stream()
                .map(ASTexte::new)
                .toArray(ASTexte[]::new))));

    }

    public void utiliserModule(String nomModule) {
        if (nomModule.equals("builtins")) {
            new ASCErrors.AlerteUtiliserBuiltins("Il est inutile d'utiliser builtins, puisqu'il est utilise par defaut");
            return;
        }

        // module vide servant à charger les fonctionnalitées expérimentales
        if (nomModule.equals("experimental")) {
            return;
        }
        Module module = getModule(nomModule);

        module.utiliser(nomModule);
        ASScope.getCurrentScope().declarerVariable(new ASConstante(nomModule, new ASListe(module
                .getNomsConstantesEtFonctions()
                .stream()
                .map(e -> nomModule + "." + e)
                .map(ASTexte::new)
                .toArray(ASTexte[]::new))));
    }

    /**
     * @param nomModule <li>nom du module a utiliser</li>
     * @param methodes  <li></li>
     */
    public void utiliserModule(String nomModule, String[] methodes) {
        if (nomModule.equals("builtins")) {
            new ASCErrors.AlerteUtiliserBuiltins("Il est inutile d'utiliser builtins, puisque le module builtins est utilise par defaut");
            return;
        }

        Module module = getModule(nomModule);

        List<String> nomsFctEtConstDemandees = Arrays.asList(methodes);

        List<String> fctEtConstPasDansModule = new ArrayList<>(nomsFctEtConstDemandees);
        fctEtConstPasDansModule.removeAll(module.getNomsConstantesEtFonctions());

        if (fctEtConstPasDansModule.size() > 0)
            throw new ASCErrors.ErreurModule("Le module '" + nomModule + "' ne contient pas les fonctions ou les constantes: "
                                             + fctEtConstPasDansModule.toString()
                    .replaceAll("\\[|]", ""));

        module.utiliser(nomsFctEtConstDemandees);
        ASScope.getCurrentScope().declarerVariable(new ASConstante(nomModule, new ASListe(nomsFctEtConstDemandees
                .stream()
                .map(ASTexte::new)
                .toArray(ASTexte[]::new))));
    }


    public Module getModule(String nomModule) {
        ModuleFactory module;
        try {
            module = MODULE_FACTORY.get(EnumModule.valueOf(nomModule));
        } catch (IllegalArgumentException err) {
            throw new ASCErrors.ErreurModule("Le module '" + nomModule + "' n'existe pas");
        }
        return module.load(executorInstance);
    }
}



























