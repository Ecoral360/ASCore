package org.ascore.generators.lexer;

import org.ascore.tokens.Token;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Classe responsable de charger les {@link TokenRule} du langage avec lesquelles seront contruits les
 * {@link Token Token}
 *
 * @author Mathis Laroche
 */
public class LexerLoader extends LexerGenerator {
    private final Map<String, Object> dict;

    public LexerLoader(String fileName) {
        if (fileName == null) fileName = "ascore/grammar_rules/Grammar.yaml";

        Yaml yaml = new Yaml();

        InputStream input = this.getClass().getResourceAsStream(fileName);
        if (input == null) {
            throw new RuntimeException("File not found: " + fileName);
        }
        try {
            this.dict = yaml.load(input);
        } catch (YAMLException exception) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw exception;
        }
    }

    public LexerLoader(InputStream stream) {
        this.dict = new Yaml().load(stream);
    }

    public Map<String, Object> getDict() {
        return dict;
    }

    @SuppressWarnings("unchecked")
    public Map<String, ?> getAddedRules() {

        return Objects.requireNonNullElse(
                (Map<String, ?>) dict.get("Ajouter"),
                (Map<String, ?>) dict.get("Add")
        );
    }

    @SuppressWarnings("unchecked")
    public Map<String, ?> getIgnoredRules() {
        return Objects.requireNonNullElse(
                (Map<String, ?>) dict.get("Ignorer"),
                (Map<String, ?>) dict.get("Ignore")
        );
    }

    @SuppressWarnings("unchecked")
    public void load() {
        var regles_a_ajouter = (Map<String, Object>) dict.get("Ajouter");
        if (regles_a_ajouter == null) {
            regles_a_ajouter = (Map<String, Object>) dict.get("Add");
        }
        regles_a_ajouter.put("@END_STATEMENT", dict.get("EndStatement"));

        for (String toAdd : regles_a_ajouter.keySet()) {
            if (regles_a_ajouter.get(toAdd) instanceof Map<?, ?>) {
                Map<String, ?> categorie = (Map<String, ?>) regles_a_ajouter.get(toAdd);
                for (String element : categorie.keySet()) {
                    this.ajouterRegle(element, (String) categorie.get(element), toAdd);
                }
            } else {
                this.ajouterRegle(toAdd, (String) regles_a_ajouter.get(toAdd), "");
            }
        }

        var regles_a_ignorer = (List<String>) dict.get("Ignorer");
        if (regles_a_ignorer == null) {
            regles_a_ignorer = (List<String>) dict.get("Ignore");
        }
        regles_a_ignorer.forEach(this::ignorerRegle);
    }
}
