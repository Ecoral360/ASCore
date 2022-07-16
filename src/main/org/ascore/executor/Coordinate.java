package org.ascore.executor;


import java.util.Objects;

/**
 * Classe utilis\u00E9e pour naviguer le code lors de l'ex\u00E9cution
 *
 * @author Mathis Laroche
 */
public class Coordinate {
    private String coord;
    private int numLigne;

    public Coordinate(String coord) {
        this.coord = coord;
        this.numLigne = -1;
    }

    Coordinate(String coord, int ligne) {
        this.coord = coord;
        this.numLigne = ligne;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    public void setNumLigne(int numLigne) {
        this.numLigne = numLigne;
    }

    /**
     * @return le scope dans lequel se situe la coordonnee
     */
    public String getScope() {
        return coord.substring(coord.lastIndexOf(">") + 1);
    }

    /**
     * @return le bloc le plus recent dans lequel se situe la coordonne
     */
    public String getBlocActuel() {
        if (coord.indexOf("<", 1) == -1) return getScope();
        else return coord.substring(coord.indexOf(">") + 1, coord.indexOf("<", 1));
    }

    /**
     * @return la boucle la plus recente dans lequel se situe la coordonne
     */
    public String getBoucleActuelle() {
        int lastIdx = coord.length();
        String boucleActuelle = null;
        for (Boucle boucle : Boucle.values()) {
            int idx = coord.indexOf(boucle.getNom());
            if (idx != -1 && idx < lastIdx) {
                lastIdx = idx;
                boucleActuelle = boucle.getNom();
            }
        }
        return boucleActuelle;
    }

    /**
     * @param nomNouveauBloc <li>nom du bloc qui va remplacer le bloc actuel</li>
     * @return la nouvelle coordonnee avec le bloc remplacer
     */
    public Coordinate remplacerBlocActuel(String nomNouveauBloc) {
        finBloc();
        nouveauBloc(nomNouveauBloc);
        return this;
    }

    /**
     * recommence le bloc actuel
     *
     * @return la coordonnee avec le bloc recommencer
     */
    public Coordinate recommencerLeBlocActuel() {
        finBloc();
        moinsUn();
        return this;
    }

    /**
     * recommence la boucle actuelle
     */
    public void recommencerBoucleActuelle() {
        while (!getBlocActuel().equals(getBoucleActuelle()) && getBoucleActuelle() != null) {
            finBloc();
        }
        recommencerLeBlocActuel();
    }

    /**
     * ajoute un bloc a la coordonnee
     *
     * @param nom <li>nom du nouveau bloc</li>
     * @return la coordonne avec le nouveau bloc
     */
    public Coordinate nouveauBloc(String nom) {
        coord = "<0>" + nom + coord;
        return this;
    }

    /**
     * retire le bloc actuel de la coordonnee
     *
     * @return la coordonnee avec le bloc actuel en moins
     */
    public Coordinate finBloc() {
        coord = coord.replaceFirst("<\\d+>\\w+", "");
        return this;
    }

    /**
     * ajoute un a la coordonnee (dans le bloc actuel)
     *
     * @return la nouvelle coordonnee
     */
    public Coordinate plusUn() {
        String premierNum = coord.substring(coord.indexOf("<") + 1, coord.indexOf(">"));
        int nextNum = Integer.parseInt(premierNum) + 1;
        coord = "<" + nextNum + coord.substring(coord.indexOf(">"));
        return this;
    }

    /**
     * retire un a la coordonnee (dans le bloc actuel)
     *
     * @return la nouvelle coordonnee
     */
    public Coordinate moinsUn() {
        String premierNum = coord.substring(coord.indexOf("<") + 1, coord.indexOf(">"));
        int nextNum = Integer.parseInt(premierNum) - 1;
        coord = "<" + nextNum + coord.substring(coord.indexOf(">"));
        return this;
    }

    @Override
    public String toString() {
        return coord;
    }

    public int getLigne() {
        return numLigne;
    }

    public Coordinate copy() {
        return new Coordinate(this.coord, this.numLigne);
    }

    public Coordinate copy(int numLigne) {
        return new Coordinate(this.coord, numLigne);
    }

    public boolean isSubCoordinate(Coordinate coord) {
        return coord.coord.endsWith(this.coord);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate that)) return false;
        return numLigne == that.numLigne &&
               Objects.equals(coord, that.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, numLigne);
    }

    enum Boucle {
        POUR("pour"),
        TANT_QUE("tant_que"),
        FAIRE("faire"),
        REPETER("repeter");

        private final String nom;

        Boucle(String nom) {
            this.nom = nom;
        }

        public String getNom() {
            return nom;
        }
    }
}


























