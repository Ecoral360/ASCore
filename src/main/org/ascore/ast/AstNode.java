package org.ascore.ast;

import org.ascore.ast.buildingBlocs.Expression;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Mathis Laroche
 */
public abstract class AstNode<T> implements BiFunction<List<Object>, Integer, T> {
    private final Hashtable<String, AstNode<? extends Expression<?>>> subAsts = new Hashtable<>();
    private int importance;

    public AstNode() {
        this.importance = -1;
    }

    public AstNode(int importance) {
        this.importance = importance;
    }

    @SafeVarargs
    public AstNode(Map.Entry<String, AstNode<? extends Expression<?>>>... subAsts) {
        this();
        for (var sous_ast : subAsts) {
            this.subAsts.put(sous_ast.getKey(), sous_ast.getValue());
        }
    }

    @SafeVarargs
    public AstNode(int importance, Map.Entry<String, AstNode<? extends Expression<?>>>... subAsts) {
        this(importance);
        for (var sous_ast : subAsts) {
            this.subAsts.put(sous_ast.getKey(), sous_ast.getValue());
        }
    }

    public static <T> AstNode<T> from(int importance, BiFunction<List<Object>, Integer, T> function) {
        return new AstNode<>(importance) {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p, idxVariante);
            }
        };
    }

    public static <T> AstNode<T> from(int importance, Function<List<Object>, T> function) {
        return new AstNode<>(importance) {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p);
            }
        };
    }

    public static <T> AstNode<T> from(BiFunction<List<Object>, Integer, T> function) {
        return new AstNode<>() {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p, idxVariante);
            }
        };
    }

    public static <T> AstNode<T> from(Function<List<Object>, T> function) {
        return new AstNode<>() {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p);
            }
        };
    }


    public Hashtable<String, AstNode<? extends Expression<?>>> getSousAst() {
        return this.subAsts;
    }

    public int getImportance() {
        return this.importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    @Override
    public abstract T apply(List<Object> p, Integer idxVariante);
}