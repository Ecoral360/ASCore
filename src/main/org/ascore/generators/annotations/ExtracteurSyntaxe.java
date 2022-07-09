package org.ascore.generators.annotations;

public final class ExtracteurSyntaxe<T> {
//    private final T instance;
//    private final Class<T> clazz;
//    private final CtClass cc;

    @SuppressWarnings("unchecked")
    public ExtracteurSyntaxe(T instance) {
//        this.instance = instance;
//        this.clazz = (Class<T>) instance.getClass();
//        ClassPool pool = ClassPool.getDefault();
//        System.out.println(pool);
//        String name = clazz.getSimpleName();
//        System.out.println(name);
//        System.out.println(pool.getCtClass(name));
//        cc = pool.getOrNull(clazz.getCanonicalName());

    }
//
//    /**
//     * @return Une Map des patterns et des fonctions défénissant les expressions dans une classe
//     */
//    public Map<String, BiFunction<List<Object>, Integer, ? extends Expression<?>>> getMapExpressions() {
//        var resultat = new LinkedHashMap<String, BiFunction<List<Object>, Integer, ? extends Expression<?>>>();
//        SortedMap<Integer, Method> methodesEnOrdre = getMethodesEnOrdre(SyntaxeExpression.class, Expression.class);
//        methodesEnOrdre.values().forEach(method -> {
//            SyntaxeExpression syntaxeExpression = method.getAnnotation(SyntaxeExpression.class);
//            resultat.put(syntaxeExpression.value(), (p, idxVariante) -> (Expression<?>) wrapMethode(method, p, idxVariante));
//        });
//        return resultat;
//    }
//
//
//    public Map<String, BiFunction<List<Object>, Integer, ? extends Programme>> getMapProgrammes() {
//        var resultat = new LinkedHashMap<String, BiFunction<List<Object>, Integer, ? extends Programme>>();
//        SortedMap<Integer, Method> methodesEnOrdre = getMethodesEnOrdre(SyntaxeProgramme.class, Programme.class);
//        methodesEnOrdre.values().forEach(method -> {
//            SyntaxeProgramme syntaxeExpression = method.getAnnotation(SyntaxeProgramme.class);
//            resultat.put(syntaxeExpression.value(), (p, idxVariante) -> (Programme) wrapMethode(method, p, idxVariante));
//        });
//        return resultat;
//    }
//
//    private SortedMap<Integer, Method> getMethodesEnOrdre(Class<? extends Annotation> annotationObligatoire, Class<?> returnType) {
//        SortedMap<Integer, Method> methodesEnOrdre = Collections.emptySortedMap();
//
//        for (var method : clazz.getDeclaredMethods()) {
//            if (!method.isAnnotationPresent(annotationObligatoire)) continue;
//            if (hasInvalidFormat(method, returnType)) {
//                System.err.println("ATTENTION: la m\u00E9thode " + method.getName() + " ne poss\u00E8de pas le bon format!");
//                System.err.println("Format attendu:\n" +
//                                   "public " + returnType.getSimpleName() + " nomMethode(List<Objet> p, Integer indexVariante) {\n" +
//                                   "\t...\n" +
//                                   "}");
//                continue;
//            }
//            CtMethod methodX = null;
//            try {
//                methodX = cc.getDeclaredMethod(method.getName());
//            } catch (NotFoundException e) {
//                e.printStackTrace();
//            }
//            if (methodX == null) continue;
//            int xlineNumber = methodX.getMethodInfo().getLineNumber(0);
//            methodesEnOrdre.put(xlineNumber, method);
//        }
//        return methodesEnOrdre;
//    }
//
//    private Object wrapMethode(Method method, List<Object> p, Integer idxVariante) {
//        try {
//            if (method.getParameters().length == 2) {
//                return method.invoke(instance, p, idxVariante);
//            }
//            return method.invoke(instance, p);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//            throw new ASErreur.ErreurAliveScript(
//                    "Erreur interne. Si vous obtenez cette erreur " +
//                    "et que vous ne travaillez pas sur le code source d'AliveScript, " +
//                    "ce n'est probablement pas de votre faute. (m\u00E9thode '" + method.getName() + "')",
//                    "ErreurCompilation"
//            );
//        }
//    }
//
//
//    private boolean hasInvalidFormat(Method method, Class<?> returnType) {
//        Parameter[] parametres = method.getParameters();
//        if (parametres.length == 0 || parametres.length > 2) return true;
//
//        // check if the return type (must be same as the value of `returnType`)
//        // and the first parameter (must be List<Object>) are valid
//        boolean valid = parametres[0].getType().equals(List.class) && returnType.isAssignableFrom(method.getReturnType());
//
//        if (parametres.length == 2) {
//            // check if the second parameter is the correct type (Integer)
//            valid = valid && parametres[1].getType().equals(Integer.class);
//        }
//        // returns if the format is invalid
//        return !valid;
//    }

}
