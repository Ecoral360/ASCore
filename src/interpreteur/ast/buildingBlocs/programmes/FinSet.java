package interpreteur.ast.buildingBlocs.programmes;

import interpreteur.as.Objets.Scope;
import interpreteur.as.erreurs.ASErreur;
import interpreteur.as.Objets.ASObjet;
import interpreteur.ast.buildingBlocs.Programme;
import interpreteur.executeur.Coordonnee;
import interpreteur.executeur.Executeur;
import interpreteur.tokens.Token;

import java.util.List;

public class FinSet extends Programme {

    public FinSet(Executeur executeurInstance) {
        super(executeurInstance);
        Scope.popCurrentScope();
    }

    @Override
    public ASObjet.Nul execute() {
        return new ASObjet.Nul();
    }

    @Override
    public Coordonnee prochaineCoord(Coordonnee coord, List<Token> ligne) {
        String nomFermeture = "set_";
        if (!coord.getScope().startsWith(nomFermeture))
            throw new ASErreur.ErreurFermeture(coord.getScope(), "fin set");
        return new Coordonnee(executeurInstance.finScope());
    }

    @Override
    public String toString() {
        return "FinFonction";
    }
}

