package org.ascore.generators.ast;

import org.ascore.ast.AstNode;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.generators.lexer.LexerGenerator;
import org.ascore.utils.Pair;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public record AstFrame(
        Pair<Hashtable<String, AstNode<? extends Statement>>, ArrayList<String>> statements,
        Pair<Hashtable<String, AstNode<? extends Expression<?>>>, ArrayList<String>> expressions
) {
    public AstFrame() {
        this(
                new Pair<>(new Hashtable<>(), new ArrayList<>()),  // statements
                new Pair<>(new Hashtable<>(), new ArrayList<>())   // expressions
        );
    }

    public Hashtable<String, AstNode<? extends Statement>> statementsDict() {
        return statements.first();
    }

    public Hashtable<String, AstNode<? extends Expression<?>>> expressionsDict() {
        return expressions.first();
    }

    public ArrayList<String> statementsOrder() {
        return statements.second();
    }

    public ArrayList<String> expressionsOrder() {
        return expressions.second();
    }

    public void addStatement(String pattern, AstNode<? extends Statement> fonction) {
        pattern = LexerGenerator.remplaceCategoriesByMembers(pattern);
        if (fonction.getImportance() == -1)
            fonction.setImportance(statementsOrder().size());
        var previous = statementsDict().put(pattern, fonction);
        if (previous == null) {
            statementsOrder().add(fonction.getImportance(), pattern);
        }
    }

    public void addStatement(String pattern, Function<List<Object>, ? extends Statement> fonction) {
        addStatement(pattern, AstNode.from(fonction));
    }

    public void addStatement(String pattern, BiFunction<List<Object>, Integer, ? extends Statement> fonction) {
        addStatement(pattern, AstNode.from(fonction));
    }

    public void addExpression(String pattern, AstNode<? extends Expression<?>> fonction) {
        String nouveauPattern = LexerGenerator.remplaceCategoriesByMembers(pattern);
        if (fonction.getImportance() == -1)
            fonction.setImportance(expressionsOrder().size());
        var previous = expressionsDict().put(nouveauPattern, fonction);
        if (previous == null) {
            expressionsOrder().add(fonction.getImportance(), nouveauPattern);
        }
    }

    public void addExpression(String pattern, Function<List<Object>, ? extends Expression<?>> fonction) {
        addExpression(pattern, AstNode.from(fonction));
    }

    public void addExpression(String pattern, BiFunction<List<Object>, Integer, ? extends Expression<?>> fonction) {
        addExpression(pattern, AstNode.from(fonction));
    }
}
