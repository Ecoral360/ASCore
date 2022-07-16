package org.ascore.lang.objects;

import org.ascore.errors.ASCErrors;

import java.util.function.Function;
import java.util.function.Supplier;

public class ASConstante extends ASVariable {

    public ASConstante(String nom, ASObjet<?> valeur) {
        super(nom, valeur, new ASType("tout"));
    }

    @Override
    public ASVariable clone() {
        return new ASConstante(obtenirNom(), this.getValeur());
    }

    @Override
    public ASVariable setSetter(Function<ASObjet<?>, ASObjet<?>> setter) {
        throw new ASCErrors.ErreurAssignement("Les constantes ne peuvent pas avoir de setter");
    }

    @Override
    public ASVariable setGetter(Supplier<ASObjet<?>> getter) {
        throw new ASCErrors.ErreurAssignement("Les constantes ne peuvent pas avoir de getter");
    }

    @Override
    public void changerValeur(ASObjet<?> valeur) {
        if (this.getValeur() != null)
            throw new ASCErrors.ErreurAssignement("Il est impossible de changer la valeur d'une constante");
        super.changerValeur(valeur);
    }
}
