package interpreteur.ast.buildingBlocs.programmes;

import interpreteur.as.Objets.ASObjet;
import interpreteur.ast.buildingBlocs.Programme;

public class FinStructure extends Programme {

    public FinStructure() {
        ASObjet.FonctionManager.retirerStructure();
    }

    @Override
    public Object execute() {
        return null;
    }

    @Override
    public String toString() {
        return "FinStructure";
    }
}
