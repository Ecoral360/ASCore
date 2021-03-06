package org.ascore.lang.objects.datatype;

import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.lang.objects.ASObjet;
import org.ascore.executor.Coordinate;
import org.ascore.lang.objects.ASParametre;
import org.ascore.lang.objects.ASScope;
import org.ascore.lang.objects.ASType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class ASFonction implements ASObjet<Object> {

    private final ASType typeRetour;
    private final ASParametre[] parametres;
    private final String nom;
    private ASScope scope;
    private String coordBlocName;
    private final ASCExecutor executorInstance;

    /**
     * @param nom        <li>
     *                   Nom de la fonction
     *                   </li>
     * @param typeRetour <li>
     *                   Nom du type de retour de la fonction (ex: <i>entier</i>, <i>texte</i>, <i>liste</i>, ect.)
     *                   </li>
     *                   <li>
     *                   le type du retour peut avoir plusieurs types
     *                   -> separer chaque type par un <b>|</b> (les espaces sont ignores)
     *                   <br> (ex: <i>texte | liste</i>, <i>entier | decimal</i>)
     *                   </li>
     *                   <li>
     *                   Mettre <b>null</b> si le type du retour n'a pas de type forcee
     *                   </li>
     */
    public ASFonction(String nom, ASType typeRetour, ASCExecutor executorInstance) {
        this.nom = nom;
        this.coordBlocName = "fonc_";
        this.typeRetour = typeRetour;
        this.parametres = new ASParametre[0];
        this.executorInstance = executorInstance;
    }

    /**
     * @param nom        <li>
     *                   Nom de la fonction
     *                   </li>
     * @param parametres
     * @param typeRetour <li>
     *                   Nom du type de retour de la fonction (ex: <i>entier</i>, <i>texte</i>, <i>liste</i>, ect.)
     *                   </li>
     *                   <li>
     *                   le type du retour peut avoir plusieurs types
     *                   -> separer chaque type par un <b>|</b> (les espaces sont ignores)
     *                   <br> (ex: <i>texte | liste</i>, <i>entier | decimal</i>)
     *                   </li>
     *                   <li>
     *                   Mettre <b>null</b> si le type du retour n'a pas de type forcee
     *                   </li>
     */
    public ASFonction(String nom, ASParametre[] parametres, ASType typeRetour, ASCExecutor executorInstance) {
        this.nom = nom;
        this.coordBlocName = "fonc_";
        this.parametres = parametres;
        this.typeRetour = typeRetour;
        this.executorInstance = executorInstance;
    }

    public String getNom() {
        return nom;
    }

    public ASType getTypeRetour() {
        return this.typeRetour;
    }

    public ASParametre[] getParams() {
        return this.parametres;
    }

    public ASScope getScope() {
        return scope;
    }

    public void setScope(ASScope scope) {
        this.scope = scope;
    }

    /**
     * @return true -> si les parametres sont initialisees <br> false -> s'il n'y a pas de parametres
     * @throws Error une erreur si un des tests n'est pas passe
     */
    public boolean testParams(ArrayList<?> paramsValeurs) {
        if (this.parametres.length == 0 && paramsValeurs.size() == 0) return false;

        int nonDefaultParams = (int) Arrays.stream(parametres).filter(param -> param.getValeurParDefaut() == null).count();

        if (paramsValeurs.size() < nonDefaultParams || paramsValeurs.size() > this.parametres.length) {
            if (nonDefaultParams == this.parametres.length) {
                throw new ASCErrors.ErreurAppelFonction(this.nom, "Le nombre de param\u00E8tres donn\u00E9s est '" + paramsValeurs.size() +
                                                                  "' alors que la fonction en prend '" + this.parametres.length + "'");
            } else {
                throw new ASCErrors.ErreurAppelFonction(this.nom, "Le nombre de param\u00E8tres donn\u00E9s est '" + paramsValeurs.size() +
                                                                  "' alors que la fonction en prend entre '" + nonDefaultParams + "' et '" + this.parametres.length + "'");
            }

        }
        for (int i = 0; i < paramsValeurs.size(); i++) {
            ASParametre parametre = this.parametres[i];
            if (parametre.getType().noMatch(((ASObjet<?>) paramsValeurs.get(i)).obtenirNomType())) {
                throw new ASCErrors.ErreurType("Le param\u00E8tres '" + parametre.getNom() + "' est de type '" + parametre.getType().nom() +
                                               "', mais l'argument pass\u00E9 est de type '" + ((ASObjet<?>) paramsValeurs.get(i)).obtenirNomType() + "'.");
            }
        }
        return true;
    }

    public void setCoordBlocName(String coordBlocName) {
        this.coordBlocName = coordBlocName;
    }

    public FonctionInstance makeInstance() {
        return new FonctionInstance(this);
    }

    public FonctionInstance makeJavaInstance(Function<ArrayList<ASObjet<?>>, ASObjet<?>> executer) {
        return new FonctionInstance(this) {
            @Override
            public ASObjet<?> executer(ArrayList<ASObjet<?>> paramsValeurs) {
                return executer.apply(paramsValeurs);
            }
        };
    }

    @Override
    public String toString() {
        return this.nom + "(" +
                String.join(", ", Arrays.stream(this.parametres).map(p -> p.getNom() + ": " + p.obtenirNomType())
                        .toArray(String[]::new)) +
                ") " +
                "\u2192 " + this.typeRetour.nom();
    }

    @Override
    public ASFonction getValue() {
        return this;
    }

    @Override
    public boolean boolValue() {
        return true;
    }

    @Override
    public String obtenirNomType() {
        return "fonctionType";
    }


    public static class FonctionInstance implements ASObjet<Object> {
        private final ASFonction fonction;
        private final ASScope.ScopeInstance scopeInstance;
        private Coordinate coordReprise = null;

        public FonctionInstance(ASFonction fonction) {
            this.fonction = fonction;
            this.scopeInstance = fonction.getScope().makeScopeInstanceFromScopeParent();
        }

        public ASObjet<?> executer(ArrayList<ASObjet<?>> paramsValeurs) {
            if (fonction.testParams(paramsValeurs)) {

                for (int i = 0; i < fonction.parametres.length; i++) {
                    ASParametre param = fonction.parametres[i];
                    if (i < paramsValeurs.size()) {
                        scopeInstance.getVariable(param.getNom()).changerValeur(paramsValeurs.get(i));

                    } else {
                        if (param.getValeurParDefaut() == null) {
                            throw new ASCErrors.ErreurAppelFonction(fonction.nom, "L'argument: " + param.getNom() + " n'a pas re??u de valeur" +
                                                                                  "et ne poss\u00E8de aucune valeur par d\u00E9faut.");
                        }
                    }
                }
            }
            ASScope.pushCurrentScopeInstance(scopeInstance);

            Object valeur;
            ASObjet<?> asValeur;
            Coordinate ancienneCoord = fonction.executorInstance.obtenirCoordRunTime().copy();

            valeur = fonction.executorInstance.executerScope(fonction.coordBlocName + fonction.nom, null, coordReprise == null ? null : coordReprise.toString());
            if (valeur instanceof String s) {
                //System.out.println("valeur: " + valeur);
                coordReprise = fonction.executorInstance.obtenirCoordRunTime().copy();
                fonction.executorInstance.setCoordRunTime(ancienneCoord.toString());
                throw new ASCErrors.StopSendData(s);

            } else {
                asValeur = (ASObjet<?>) valeur;
            }

            coordReprise = null;

            /*Boucle.sortirScope(fonction.executeurInstance.obtenirCoordRunTime().toString());*/

            fonction.executorInstance.setCoordRunTime(ancienneCoord.toString());
            ASScope.popCurrentScopeInstance();

            if (asValeur == null || fonction.typeRetour.noMatch(asValeur.obtenirNomType())) {
                throw new ASCErrors.ErreurType("Le type retourner ' " + (asValeur == null ? "vide" : asValeur.obtenirNomType()) + " ' ne correspond pas "
                                               + "au type de retour pr\u00E9cis\u00E9 dans la d\u00E9claration de la fonction ' " + fonction.typeRetour.nom() + " '.");

            }
            return asValeur;
        }

        @Override
        public Object getValue() {
            return this;
        }

        @Override
        public boolean boolValue() {
            return fonction.boolValue();
        }

        @Override
        public String obtenirNomType() {
            return fonction.obtenirNomType();
        }

        @Override
        public String toString() {
            return fonction.toString();
        }
    }


}























