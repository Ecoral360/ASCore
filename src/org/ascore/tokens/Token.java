package org.ascore.tokens;


import org.ascore.generators.lexer.TokenRule;

import java.util.Objects;

/**
 * Les explications vont être rajouté quand j'aurai la motivation de les écrire XD
 *
 * @author Mathis Laroche
 */

public class Token {

    private final String name, value, category;
    private final int start;
    private final TokenRule tokenRuleParent;

    public Token(String name, String value, String category, int start, TokenRule tokenRuleParent) {
        this.name = name;
        this.value = value;
        this.category = category;
        this.start = start;
        this.tokenRuleParent = tokenRuleParent;
    }

    public Token(String name, String value, String category, int start) {
        this(name, value, category, start, null);
    }

    public static Token withName(String name) {
        return new Token(name, null, null, -1);
    }

    public static Token withCategory(String category) {
        return new Token(null, null, category, -1);
    }

    public static Token withValue(String value) {
        return new Token(null, value, null, -1);
    }

    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public int getStart() {
        return this.start;
    }

    public TokenRule getTokenRuleParent() {
        return tokenRuleParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token)) return false;
        if (token.getName() != null && name != null)
            return Objects.equals(name, token.name);
        if (token.getCategory() != null && category != null)
            return Objects.equals(category, token.category);
        if (token.getValue() != null && value != null)
            return Objects.equals(value, token.value);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Token{" +
               "nom='" + name + '\'' +
               ", valeur='" + value + '\'' +
               (!category.isBlank() ? ", categorie='" + category + '\'' : "") +
               ", debut=" + start +
               (tokenRuleParent != null ? ", pattern=" + tokenRuleParent.getPattern() : "") +
               '}';
    }
}
















