package ascore.as.Objets;

import ascore.as.Objets.interfaces.ASObjet;
import ascore.as.Objets.managers.FonctionManager;
import ascore.as.erreurs.ASErreur;

import java.util.function.Function;
import java.util.function.Supplier;

public class Variable implements ASObjet<Object> {
    private final String nom;
    private final Type type;
    private ASObjet<?> valeur;
    private boolean readOnly = false;

    private Supplier<ASObjet<?>> getter = null;
    private Function<ASObjet<?>, ASObjet<?>> setter = null;


    public Variable(String nom, ASObjet<?> valeur, Type type) {
        this.type = type == null ? new Type("tout") : type;
        this.nom = FonctionManager.ajouterDansStructure(nom);
        this.valeur = valeur instanceof Variable var ? var.getValeurApresGetter() : valeur;
    }

    private boolean nouvelleValeurValide(ASObjet<?> nouvelleValeur) {
        if (getType().noMatch(nouvelleValeur.obtenirNomType())) {
            throw new ASErreur.ErreurAssignement("La variable '" +
                    nom +
                    "' est de type *" +
                    obtenirNomType() +
                    "*. Elle ne peut pas prendre une valeur de type *" +
                    nouvelleValeur.obtenirNomType() +
                    "*.");
        }
        return true;
    }

    /**
     * applique le setter
     *
     * @param valeur
     */
    public void changerValeur(ASObjet<?> valeur) {
        if (nouvelleValeurValide(valeur)) {
            if (this.setter != null) {
                this.valeur = this.setter.apply(valeur);
            } else {
                this.valeur = valeur;
            }
        }
    }

    @Override
    public Variable clone() {
        return new Variable(nom, this.valeur, this.type).setGetter(this.getter).setSetter(this.setter);
    }

    public String obtenirNom() {
        return this.nom;
    }

    public Type getType() {
        return type;
    }

    public boolean pasInitialisee() {
        return this.valeur == null;
    }

    public Variable setGetter(Supplier<ASObjet<?>> getter) {
        this.getter = getter;
        return this;
    }

    public Variable setSetter(Function<ASObjet<?>, ASObjet<?>> setter) {
        this.setter = setter;
        return this;
    }

    public Variable setReadOnly() {
        this.setter = (valeur) -> {
            throw new ASErreur.ErreurAssignement("Cette variable est en lecture seule: elle ne peut pas \u00EAtre modifi\u00E9e");
        };
        this.readOnly = true;
        return this;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", valeur=" + valeur +
                ", getter=" + getter +
                ", setter=" + setter +
                '}';
    }

    /* différentes manières de get la valeur stockée dans la variable */
    public ASObjet<?> getValeur() {
        return this.valeur;
    }

    /**
     * by pass the setter
     *
     * @param valeur
     */
    public void setValeur(ASObjet<?> valeur) {
        if (nouvelleValeurValide(valeur))
            this.valeur = valeur;
    }

    public ASObjet<?> getValeurApresGetter() {
        if (this.valeur == null) {
            throw new ASErreur.ErreurAssignement("La variable '" + nom + "' est utilis\u00E9e avant d'\u00EAtre d\u00E9clar\u00E9e");
        }
        if (this.getter != null) {
            return this.getter.get();
        }
        return this.valeur;
    }

    @Override
    public Object getValue() {
        if (this.valeur == null) {
            throw new ASErreur.ErreurAssignement("La variable '" + nom + "' est utilis\u00E9e avant d'\u00EAtre d\u00E9clar\u00E9e");
        }
        if (this.getter != null) {
            return this.getter.get().getValue();
        }
        return this.valeur.getValue();
    }

    @Override
    public boolean boolValue() {
        return this.valeur.boolValue();
    }

    @Override
    public String obtenirNomType() {
        return this.type.getNom();
    }
}
