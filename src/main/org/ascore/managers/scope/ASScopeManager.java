package org.ascore.managers.scope;

import org.ascore.lang.objects.ASScope;
import org.jetbrains.annotations.Contract;

import java.util.Stack;

public class ASScopeManager {
    public enum ScopeKind {
        STRUCTURE("struct"),
        FONCTION("fonc"),
        GETTER("get"),
        SETTER("set");

        private final String start;

        ScopeKind(String start) {
            this.start = start;
        }

        private static ScopeKind fromString(String start) {
            for (var kind : ScopeKind.values()) {
                if (kind.start.equals(start)) return kind;
            }
            return null;
        }
    }

    private static final String SCOPE_SEPARATOR = "@";
    private static final String SCOPE_START_SEPARATOR = "~";

    private final Stack<ASScope> scopeStack = new Stack<>();
    private final Stack<ASScope.ScopeInstance> scopeInstanceStack = new Stack<>();

    @Contract(pure = true)
    public static ScopeKind getScopeKind(String scope) throws IllegalArgumentException {
        if (!scope.contains(SCOPE_START_SEPARATOR)) throw new IllegalArgumentException(scope);
        return ScopeKind.fromString(scope.split(SCOPE_START_SEPARATOR, 2)[0]);
    }

    @Contract(pure = true)
    public static String getScopeName(String scope) throws IllegalArgumentException {
        return scope.substring(scope.lastIndexOf(SCOPE_SEPARATOR) + 1);
    }

    @Contract(pure = true)
    public static String formatNewScope(ScopeKind scopeKind, String currentScope, String newScopeName) {
        return scopeKind.start + SCOPE_START_SEPARATOR + currentScope + SCOPE_SEPARATOR + newScopeName;
    }


    /**
     * Crée un nouveau scope puis le met comme <code>currentScope</code>
     *
     * @return the new scope
     */
    public ASScope makeNewCurrentScope() {
        ASScope scope = new ASScope();
        updateCurrentScope(scope);
        return scope;
    }

    public Stack<ASScope> getScopeStack() {
        return scopeStack;
    }

    public ASScope getCurrentScope() {
        return scopeStack.peek();
    }

    public void updateCurrentScope(ASScope scope) {
        scopeStack.push(scope);
    }

    public void popCurrentScope() {
        scopeStack.pop();
    }


    public Stack<ASScope.ScopeInstance> getScopeInstanceStack() {
        return scopeInstanceStack;
    }

    public ASScope.ScopeInstance getCurrentScopeInstance() {
        return scopeInstanceStack.peek();
    }

    public void pushCurrentScopeInstance(ASScope.ScopeInstance scopeInstance) {
        scopeInstanceStack.push(scopeInstance);
    }

    public void popCurrentScopeInstance() {
        scopeInstanceStack.pop();
    }

    public void resetAllScope() {
        scopeInstanceStack.clear();
        scopeStack.clear();
    }

}
