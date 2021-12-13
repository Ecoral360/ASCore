package ascore.as.lang.datatype;

import ascore.as.erreurs.ASErreur;
import ascore.tokens.Token;

import java.util.Objects;

public class Booleen implements ASObjet<Boolean> {
    private final boolean valeur;

    public Booleen(Token valeur) {
        this.valeur = valeur.getValeur().equals("vrai");
    }

    public Booleen(ASObjet<?> valeur) {
        this.valeur = Boolean.parseBoolean(valeur.getValue().toString());
    }

    public Booleen(Boolean valeur) {
        this.valeur = valeur;
    }

    public Booleen(String valeur) {
        this.valeur = switch (valeur) {
            case "vrai" -> true;
            case "faux" -> false;
            default -> throw new ASErreur.ErreurType("La valeur " + valeur + " ne peut pas \u00EAtre convertie en bool\u00E9en.");
        };
    }

    public static boolean estBooleen(String txt) {
        return txt.equals("vrai") || txt.equals("faux");
    }

    @Override
    public String toString() {
        return valeur ? "vrai" : "faux";
    }

    @Override
    public Boolean getValue() {
        return valeur;
    }

    @Override
    public boolean boolValue() {
        return this.getValue();
    }

    @Override
    public String obtenirNomType() {
        return "booleen";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booleen booleen)) return false;
        return valeur == booleen.valeur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur);
    }
}
