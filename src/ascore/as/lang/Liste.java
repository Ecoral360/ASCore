package ascore.as.lang;

import ascore.as.lang.interfaces.ASObjet;
import ascore.as.lang.interfaces.Iterable;
import ascore.as.erreurs.ASErreur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Liste implements Iterable<Object> {
    private ArrayList<ASObjet<?>> valeur = new ArrayList<>();

    public Liste() {
    }

    public Liste(ASObjet<?>... valeurs) {
        this.valeur = new ArrayList<>(Arrays.asList(valeurs));
        aucuneClefDuplique();
    }

    public void ajouterElement(ASObjet<?> element) {
        clefValideOrThrow(element);
        this.valeur.add(element);
    }

    public ascore.as.lang.Liste ajouterTout(ascore.as.lang.Liste elements) {
        elements.getValue().forEach(this::clefValideOrThrow);
        this.valeur.addAll(elements.getValue());
        return this;
    }

    public boolean estDict() {
        return valeur.stream().allMatch(ASPaire.class::isInstance);
    }

    public void clefValideOrThrow(ASObjet<?> nouvelElement) {
        if (!(nouvelElement instanceof ASPaire nouvellePaire))
            return;
        if (valeur.stream().anyMatch(val -> val instanceof ASPaire paire && paire.clef().equals(nouvellePaire.clef()))) {
            throw new ASErreur.ErreurClefDupliquee("La clef " + nouvellePaire.clef() + " existe d\u00E9j\u00e0 dans le dictionnaire ou la liste");
        }
    }

    public void aucuneClefDuplique() {
        var clefs = valeur.stream()
                .map(val -> val instanceof ASPaire paire ? paire.clef().getValue() : null)
                .filter(Objects::nonNull).toList();
        if (clefs.stream().distinct().count() != clefs.size()) {
            throw new ASErreur.ErreurClefDupliquee("Il y a au moins une clef dupliqu\u00E9e dans le dictionnaire ou la liste");
        }
    }

    public void retirerElement(int idx) {
        ASObjet<?> element = this.valeur.remove(idxRelatif(this.valeur, idx));
    }

    public ascore.as.lang.Liste remplacer(int idx, ASObjet<?> valeur) {
        this.valeur.set(idxRelatif(this.valeur, idx), valeur);
        aucuneClefDuplique();
        return this;
    }

    public ascore.as.lang.Liste remplacer(ASObjet<?> valeur, ASObjet<?> remplacement) {
        this.valeur.replaceAll(v -> v.equals(valeur) ? remplacement : v);
        aucuneClefDuplique();
        return this;
    }

    public ascore.as.lang.Liste remplacerRange(int debut, int fin, ascore.as.lang.Liste remplacement) {
        debut = idxRelatif(valeur, debut);
        fin = idxRelatif(valeur, fin);
        this.valeur = this.sousSection(0, debut).ajouterTout(remplacement).ajouterTout(this.sousSection(fin, this.taille())).getValue();
        aucuneClefDuplique();
        return this;
    }

    public ArrayList<?> map(Function<ASObjet<?>, ?> mappingFunction) {
        var result = this.valeur.stream().map(mappingFunction).collect(Collectors.toCollection(ArrayList::new));
        aucuneClefDuplique();
        return result;
    }

    public ASObjet<?> get(Texte texte) {
        return valeur.stream()
                .filter(val -> val instanceof ASPaire pair && pair.clef().equals(texte))
                .findFirst()
                .orElse(new ValeurNul());
    }

    @Override
    public ASObjet<?> get(int idx) {
        return this.valeur.get(idxRelatif(this.valeur, idx));
    }

    @Override
    public int taille() {
        return this.valeur.size();
    }

    @Override
    public Iterator<ASObjet<?>> iter() {
        return this.valeur.iterator();
    }

    @Override
    public boolean contient(ASObjet<?> element) {
        return this.valeur.contains(element)
                ||
                (element instanceof Texte texte && this.valeur.stream().anyMatch(val -> val instanceof ASPaire pair && pair.clef().equals(texte)));
    }

    @Override
    public SousListe sousSection(int debut, int fin) {
        return new SousListe(this, debut, idxRelatif(this.valeur, fin));
    }

    @Override
    public String toString() {
        AtomicInteger nbPair = new AtomicInteger(0);

        String toString = String.join(", ", this.valeur
                .stream()
                .map(e -> {
                    if (e instanceof Texte) return "\"" + e + "\"";
                    else if (e instanceof ASPaire) nbPair.set(nbPair.get() + 1);
                    return e.toString();
                })
                .toArray(String[]::new));

        final char openingSymbol = nbPair.get() != taille() ? '[' : '{';
        final char closingSymbol = nbPair.get() != taille() ? ']' : '}';

        return openingSymbol + toString + closingSymbol;
    }

    @Override
    public ArrayList<ASObjet<?>> getValue() {
        return this.valeur;
    }

    @Override
    public boolean boolValue() {
        return !this.valeur.isEmpty();
    }

    @Override
    public String obtenirNomType() {
        return estDict() ? "dict" : "liste";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ascore.as.lang.Liste liste)) return false;
        return Objects.equals(valeur, liste.valeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur);
    }

    public static class SousListe extends ascore.as.lang.Liste {
        private final ascore.as.lang.Liste parent;

        SousListe(ascore.as.lang.Liste parent, int debut, int fin) {
            super(parent.valeur.subList(debut, fin).toArray(ASObjet<?>[]::new));
            this.parent = parent;
        }

        public ascore.as.lang.Liste getParent() {
            return parent;
        }
    }
}
