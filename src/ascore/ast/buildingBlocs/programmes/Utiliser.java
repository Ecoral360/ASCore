package ascore.ast.buildingBlocs.programmes;

import ascore.as.lang.FonctionModule;
import ascore.ast.buildingBlocs.Programme;
import ascore.ast.buildingBlocs.expressions.Var;
import ascore.executeur.Executeur;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exemple d'un {@link Programme} responsable de charger un module au <i>Compile time</i> selon le nom de la variable
 * qui lui a \u00E9t\u00E9 pr\u00E9cis\u00E9e
 *
 * @author Mathis Laroche
 */
public class Utiliser extends Programme {
    private final Var module;
    private final List<Var> sous_modules;

    /**
     * @param module            nom du module pr\u00E9sent \u00e0 l'int\u00E9rieur d'une expression {@link Var}
     * @param sous_modules      array des noms des {@link FonctionModule fonctions} et des
     *                          {@link ascore.as.lang.Variable variables}/{@link ascore.as.lang.Constante constantes}
     *                          \u00e0 charger dans le module
     * @param executeurInstance l'ex\u00E9cuteur actuel
     */
    public Utiliser(Var module, Var[] sous_modules, Executeur executeurInstance) {
        super(executeurInstance);
        this.module = module;
        this.sous_modules = Arrays.asList(sous_modules);
        this.chargerModule();
    }

    /**
     * @param module            nom du module pr\u00E9sent \u00e0 l'int\u00E9rieur d'une expression {@link Var}
     * @param executeurInstance l'ex\u00E9cuteur actuel
     */
    public Utiliser(Var module, Executeur executeurInstance) {
        super(executeurInstance);
        this.module = module;
        this.sous_modules = new ArrayList<>();
        this.chargerModule();
    }

    /**
     * utilise le module
     * @see ascore.as.modules.core.Module#utiliser(String) Module.utiliser
     */
    private void chargerModule() {
        if (sous_modules.isEmpty()) {
            executeurInstance.getAsModuleManager().utiliserModule(module.getNom());
        } else {
            executeurInstance.getAsModuleManager().utiliserModule(module.getNom(), sous_modules.stream().map(Var::getNom).toArray(String[]::new));
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
