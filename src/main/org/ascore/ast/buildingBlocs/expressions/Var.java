package org.ascore.ast.buildingBlocs.expressions;

import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASError;
import org.ascore.lang.objects.ASObjet;
import org.ascore.managers.scope.ASScopeManager;

import java.util.Objects;

/**
 * Exemple d'une expression charg\u00E9e de retourner la valeur d'une variable au Runtime
 * selon le nom de la variable qui lui a \u00E9t\u00E9 pr\u00E9cis\u00E9e au compile time
 *
 * @author Mathis Laroche
 */
public class Var implements Expression<ASObjet<?>> {
    private String nom;
    private final ASScopeManager scopeManager;

    public Var(String nom, ASScopeManager scopeManager) {
        this.nom = nom;
        this.scopeManager = scopeManager;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return la valeur de la variable ayant le m\u00EAme nom que {@link #nom Var.nom}
     */
    @Override
    public ASObjet<?> eval() {
        try {
            // return ASObjet.VariableManager.obtenirVariable(this.nom).getValeurApresGetter();
            return scopeManager.getCurrentScopeInstance().getVariable(nom).getValeurApresGetter();
        } catch (NullPointerException e) {
            throw new ASError.ErreurVariableInconnue("La variable '" + this.nom + "' n'est pas d\u00E9clar\u00E9e dans ce scope.");
        }
    }

    @Override
    public String toString() {
        return "Var{" +
                "nom='" + nom + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Var var)) return false;
        return nom.equals(var.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

}

