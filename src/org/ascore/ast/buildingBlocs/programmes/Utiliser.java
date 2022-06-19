package org.ascore.ast.buildingBlocs.programmes;

import org.ascore.lang.objects.ASConstante;
import org.ascore.lang.objects.ASFonctionModule;
import org.ascore.lang.objects.ASVariable;
import org.ascore.lang.modules.core.ASModule;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.ast.buildingBlocs.expressions.Var;
import org.ascore.executor.Executor;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exemple d'un {@link Statement} responsable de charger un module au <i>Compile time</i> selon le nom de la variable
 * qui lui a \u00E9t\u00E9 pr\u00E9cis\u00E9e
 *
 * @author Mathis Laroche
 */
public class Utiliser extends Statement {
    private final Var module;
    private final List<Var> sous_modules;

    /**
     * @param module            nom du module pr\u00E9sent \u00e0 l'int\u00E9rieur d'une expression {@link Var}
     * @param sous_modules      array des noms des {@link ASFonctionModule fonctions} et des
     *                          {@link ASVariable variables}/{@link ASConstante constantes}
     *                          \u00e0 charger dans le module
     * @param executorInstance l'ex\u00E9cuteur actuel
     */
    public Utiliser(Var module, Var[] sous_modules, Executor executorInstance) {
        super(executorInstance);
        this.module = module;
        this.sous_modules = Arrays.asList(sous_modules);
        this.chargerModule();
    }

    /**
     * @param module            nom du module pr\u00E9sent \u00e0 l'int\u00E9rieur d'une expression {@link Var}
     * @param executorInstance l'ex\u00E9cuteur actuel
     */
    public Utiliser(Var module, Executor executorInstance) {
        super(executorInstance);
        this.module = module;
        this.sous_modules = new ArrayList<>();
        this.chargerModule();
    }

    /**
     * utilise le module
     * @see ASModule#utiliser(String) Module.utiliser
     */
    private void chargerModule() {
        if (sous_modules.isEmpty()) {
            executorInstance.getAsModuleManager().utiliserModule(module.getNom());
        } else {
            executorInstance.getAsModuleManager().utiliserModule(module.getNom(), sous_modules.stream().map(Var::getNom).toArray(String[]::new));
        }
    }

    /**
     * Ce programme ne fait rien au <i>Runtime</i>
     * @return null
     */
    @Override
    public NullType execute() {
        return null;
    }

    @Override
    public String toString() {
        return "Utiliser{" +
                "module=" + module +
                ", sous_modules?=" + sous_modules +
                '}';
    }
}
