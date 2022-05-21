package org.ascore.ast;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Mathis Laroche
 */
public abstract class Ast<T> implements BiFunction<List<Object>, Integer, T> {
    private final Hashtable<String, Ast<?>> subAsts = new Hashtable<>();
    private int importance;

    public Ast() {
        this.importance = -1;
    }

    public Ast(int importance) {
        this.importance = importance;
    }

    @SafeVarargs
    public Ast(Map.Entry<String, Ast<?>>... subAsts) {
        this();
        for (var sous_ast : subAsts) {
            this.subAsts.put(sous_ast.getKey(), sous_ast.getValue());
        }
    }

    @SafeVarargs
    public Ast(int importance, Map.Entry<String, Ast<?>>... subAsts) {
        this(importance);
        for (var sous_ast : subAsts) {
            this.subAsts.put(sous_ast.getKey(), sous_ast.getValue());
        }
    }

    public static <T> Ast<T> from(int importance, BiFunction<List<Object>, Integer, T> function) {
        return new Ast<>(importance) {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p, idxVariante);
            }
        };
    }

    public static <T> Ast<T> from(int importance, Function<List<Object>, T> function) {
        return new Ast<>(importance) {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p);
            }
        };
    }

    public static <T> Ast<T> from(BiFunction<List<Object>, Integer, T> function) {
        return new Ast<>() {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p, idxVariante);
            }
        };
    }

    public static <T> Ast<T> from(Function<List<Object>, T> function) {
        return new Ast<>() {
            @Override
            public T apply(List<Object> p, Integer idxVariante) {
                return function.apply(p);
            }
        };
    }


    public Hashtable<String, Ast<?>> getSousAst() {
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