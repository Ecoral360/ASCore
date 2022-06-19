package org.ascore.generators.lexer;

import org.ascore.tokens.Token;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Classe charg\u00E9e de transformer un {@link String} en {@link List}<{@link Token}>
 *
 * @author Mathis Laroche
 */
public class LexerGenerator {
    static private final ArrayList<TokenRule> REGLES_IGNOREES = new ArrayList<>();
    static private ArrayList<TokenRule> reglesAjoutees = new ArrayList<>();

    public LexerGenerator() {
        reglesAjoutees.clear();
        REGLES_IGNOREES.clear();
        TokenRule.reset();
    }

    public static String remplaceCategoriesByMembers(String pattern) {
        String nouveauPattern = pattern;
        for (String option : pattern.split("~")) {
            for (String motClef : option.split(" ")) {  // on divise le pattern en mot clef afin d'evaluer ceux qui sont des categories (une categorie est entouree par des {})
                if (motClef.startsWith("{") && motClef.endsWith("}")) {  // on test si le mot clef est une categorie
                    ArrayList<String> membresCategorie = TokenRule.getMembreCategorie(motClef.substring(1, motClef.length() - 1)); // on va chercher les membres de la categorie (toutes les regles)
                    if (membresCategorie == null) {
                        throw new Error("La categorie: '" + pattern + "' n'existe pas");    // si la categorie n'existe pas, on lance une erreur
                    } else {
                        nouveauPattern = nouveauPattern.replace(motClef, "(" + String.join("|", membresCategorie) + ")");
                        // on remplace la categorie par les membres de la categorie
                        // pour ce faire, on entoure les membres dans des parentheses et on
                        // separe les membres par des |
                        // de cette facon, lorsque nous allons tester par regex si une ligne correspond
                        // a un programme ou une expression, la categorie va "matcher" avec
                        // tous les membres de celle-ci
                    }
                }
            }
        }
        return nouveauPattern;  // on retourne le pattern avec les categories changees
    }

    protected void ajouterRegle(String nom, String pattern, String categorie) {
        reglesAjoutees.add(new TokenRule(nom, pattern, categorie));
    }

    protected void sortTokenRules() {
        ArrayList<TokenRule> nomVars = reglesAjoutees.stream().filter(r -> r.getNom().equals("NOM_VARIABLE")).collect(Collectors.toCollection(ArrayList::new));
        reglesAjoutees = reglesAjoutees.stream().filter(r -> !r.getNom().equals("NOM_VARIABLE")).collect(Collectors.toCollection(ArrayList::new));

        Comparator<TokenRule> longueurRegle = (o1, o2) -> o2.getPattern().length() - o1.getPattern().length();

        reglesAjoutees.sort(longueurRegle);
        nomVars.sort(longueurRegle);

        reglesAjoutees.addAll(nomVars);
        // this.reglesAjoutees.forEach(r -> System.out.println(r.getNom() + "  " + r.getPattern()));
    }

    protected void ignorerRegle(String pattern) {
        REGLES_IGNOREES.add(new TokenRule(pattern));
    }

    public ArrayList<TokenRule> getReglesAjoutees() {
        return reglesAjoutees;
    }

    public ArrayList<TokenRule> getReglesIgnorees() {
        return REGLES_IGNOREES;
    }


    public List<Token> lex(String s) {

        List<Token> tokenList = new ArrayList<>();

        int idx = 0;
        int debut;

        while (idx < s.length()) {

            idx = this.prochainIndexValide(idx, s);

            boolean trouve = false;
            for (TokenRule tokenRule : this.getReglesAjoutees()) {
                Matcher match = Pattern.compile(tokenRule.getPattern()).matcher(s);
                if (match.find(idx) && match.start() == idx) {
                    debut = match.start();
                    idx = match.end();
                    tokenList.add(tokenRule.makeToken(s.substring(match.start(), match.end()), debut));
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                idx = ajouterErreur(idx, s, tokenList);
            }
        }
        return tokenList;
    }


    /**
     * @return le prochain index valide (ignore les patterns dans ignorerRegles)
     */
    private int prochainIndexValide(int idx, String s) {
        while (true) {
            boolean trouve = false;
            for (TokenRule tokenRule : this.getReglesIgnorees()) {
                Matcher match = Pattern.compile(tokenRule.getPattern()).matcher(s);
                if (match.find(idx) && match.start() == idx) {
                    trouve = true;
                    idx = match.end();
                }
            }
            if (!trouve) {
                break;
            }
        }
        return idx;
    }


    private int ajouterErreur(int idx, String s, List<Token> tokenList) {
        /**
         * @add le token ERREUR � la liste de token
         * @return le prochain index valide
         */
        idx = this.prochainIndexValide(idx, s);
        Matcher match = Pattern.compile("\\S+").matcher(s);
        //System.out.println("idxOrKey : " + idxOrKey);

        if (idx < s.length()) {
            match.find(idx);
            tokenList.add(new Token("(ERREUR)",
                    s.substring(match.start(), match.end()),
                    "",
                    match.start()));
            idx = match.end();
        }

        return idx;

    }
}




















