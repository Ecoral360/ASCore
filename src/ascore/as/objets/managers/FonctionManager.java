package ascore.as.objets.managers;

import ascore.as.objets.*;

public class FonctionManager {

    private static String structure = "";

    // met la fonction dans le dictionnaire de fonction et cree enregistre la fonction dans une Variable
    // pour que le code puisse la retrouver plus tard
    public static void ajouterFonction(Fonction fonction) {
        Scope.getCurrentScope().declarerVariable(new Variable(fonction.getNom(), fonction, new Type(fonction.obtenirNomType())));
        //VariableManager.ajouterConstante(new Constante(fonction.getNom(), fonction));
        //fonction.nom = ajouterDansStructure(fonction.getNom());
    }

    public static String obtenirStructure() {
        return structure;
    }

    public static String ajouterDansStructure(String element) {
        return (structure.isBlank() ? "" : structure + ".") + element;
    }

    public static void ajouterStructure(String nomStruct) {
        if (nomStruct == null) return;
        structure += (structure.isBlank() ? "" : ".") + nomStruct;
    }

    public static void retirerStructure() {
        structure = structure.contains(".") ? structure.substring(0, structure.lastIndexOf(".")) : "";

    }

    public static void reset() {
        structure = "";
    }
}
