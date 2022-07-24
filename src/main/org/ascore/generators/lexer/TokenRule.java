package org.ascore.generators.lexer;

import org.ascore.tokens.Token;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Classe responsable de cr\u00E9er des {@link Token} selon un {@link #pattern}
 *
 * @author Mathis Laroche
 */
public class TokenRule {

    static private final Hashtable<String, ArrayList<String>> categories = new Hashtable<>();

    private final String name, pattern, category;

    public TokenRule(String name, String pattern, String category) {
        this.name = name;
        this.pattern = pattern;
        this.category = category;

        categories.putIfAbsent(category, new ArrayList<>());
        categories.get(category).add(name);
    }

    public TokenRule(String pattern) {
        this.category = null;
        this.pattern = pattern;
        this.name = null;
    }

    public static void reset() {
        categories.clear();
    }

    public static Hashtable<String, ArrayList<String>> getCategories() {
        return categories;
    }

    public static ArrayList<String> getMembreCategorie(String nomCategorie) {
        return categories.get(nomCategorie);
    }

    public String getName() {
        return this.name;
    }

    public String getPattern() {
        return this.pattern;
    }

    public Pattern compileToPattern() {
        return Pattern.compile(this.pattern);
    }

    public String getCategory() {
        return this.category;
    }

    public Token makeToken(String valeur, int debut, Matcher match) {
        return new Token(this.name, valeur, this.category, debut, this, match);
    }

    @Override
    public String toString() {
        return "Regle{" +
               "nom='" + name + '\'' +
               ", pattern='" + pattern + '\'' +
               ", categorie='" + category + '\'' +
               '}';
    }
}
