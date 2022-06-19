package org.ascore.lang.modules.builtins;

import org.ascore.lang.objects.ASConstante;
import org.ascore.lang.objects.ASFonctionModule;
import org.ascore.lang.objects.ASVariable;
import org.ascore.lang.modules.core.ASModule;
import org.ascore.executor.Executor;

/**
 * Classe o\u00F9 sont d\u00E9finis les {@link ASFonctionModule fonctions} et les
 * {@link ASVariable variables}/{@link ASConstante constantes} builtin
 *
 * @author Mathis Laroche
 */
public class ModuleBuiltin {

    public static ASModule charger(Executor executorInstance) {
        var fonctionsBuiltin = new ASFonctionModule[]{
                // ajouter vos fonctions builtin ici
        };
        var variablesBuiltin = new ASVariable[]{
                // ajouter vos variables et vos constantes builtin ici
        };

        return new ASModule(fonctionsBuiltin, variablesBuiltin);
    }
}
