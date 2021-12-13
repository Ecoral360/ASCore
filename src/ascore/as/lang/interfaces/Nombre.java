package ascore.as.lang.interfaces;

import ascore.as.lang.Decimal;
import ascore.as.lang.Entier;
import ascore.as.erreurs.ASErreur;

public interface Nombre extends ASObjet<Number> {
    static boolean estNumerique(String txt) {
        try {
            var estDecimal = txt.contains(".");
            if (estDecimal) Double.parseDouble(txt);
            else Integer.parseInt(txt);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }

    static Nombre parse(ASObjet<?> nb) {
        String txt = nb.toString();
        if (!Nombre.estNumerique(txt))
            throw new ASErreur.ErreurType("Impossible de convertir " + txt + " en nombre entier ou d\u00E9cimal.");

        return txt.contains(".") ? new Decimal(Double.parseDouble(txt)) : new Entier(Integer.parseInt(txt));
    }

    static Nombre cast(Number nb) {
        return nb.doubleValue() != nb.intValue() ? new Decimal(nb) : new Entier(nb);
    }

    static Number asNumber(ASObjet<?> nb) {
        String txt = nb.toString();
        if (!Nombre.estNumerique(txt))
            throw new ASErreur.ErreurType("Impossible de convertir " + txt + " en nombre entier ou d\u00E9cimal.");

        return (Number) nb.getValue();
    }

    static Double asDouble(ASObjet<?> nb) {
        String txt = nb.toString();
        if (!Nombre.estNumerique(txt))
            throw new ASErreur.ErreurType("Impossible de convertir " + txt + " en nombre entier ou d\u00E9cimal.");

        return ((Number) nb.getValue()).doubleValue();
    }

    static Integer asInteger(ASObjet<?> nb) {
        String txt = nb.toString();
        if (!Nombre.estNumerique(txt))
            throw new ASErreur.ErreurType("Impossible de convertir " + txt + " en nombre entier ou d\u00E9cimal.");

        return ((Number) nb.getValue()).intValue();
    }

    @Override
    default String obtenirNomType() {
        return "nombre";
    }
}
