package org.ascore.lang.objects;

import java.util.Stack;

public class ASScope {
    // static fields
    private final static Stack<ASScope> scopeStack = new Stack<>();
    private final static Stack<ScopeInstance> scopeInstanceStack = new Stack<>();

    // non static fields
    private ScopeInstance parent;
    private final Stack<ASCVariable> variablesDeclarees = new Stack<>();

    public ASScope() {
        this.parent = null;
    }

    public ASScope(ASScope fromScope) {
        this.parent = null;
        this.variablesDeclarees.addAll(fromScope.cloneVariablesDeclarees());
    }

    public ASScope(ScopeInstance parent) {
        this.parent = parent;
    }

    public void setParent(ScopeInstance parent) {
        this.parent = parent;
    }

    //#region --------- static stuff ---------


    public static ASScope getCurrentScope() {
        return scopeStack.peek();
    }

    public static ScopeInstance getCurrentScopeInstance() {
        return scopeInstanceStack.peek();
    }

    public static void pushCurrentScopeInstance(ScopeInstance scopeInstance) {
        scopeInstanceStack.push(scopeInstance);
    }

    public static void popCurrentScopeInstance() {
        scopeInstanceStack.pop();
    }


    public static void resetAllScope() {
        scopeInstanceStack.clear();
        scopeStack.clear();
    }

    //#endregion


    //#region --------- not static stuff ---------

    private Stack<ASCVariable> cloneVariablesDeclarees() {
        Stack<ASCVariable> newVariableStack = new Stack<>();
        variablesDeclarees.forEach(var -> newVariableStack.push(var.clone()));
        return newVariableStack;
    }

    public Stack<ASCVariable> getVariablesDeclarees() {
        return variablesDeclarees;
    }

    public ScopeInstance makeScopeInstance(ScopeInstance parent) {
        return new ScopeInstance(parent, cloneVariablesDeclarees());
    }

    public ScopeInstance makeScopeInstanceFromCurrentScope() {
        return new ScopeInstance(getCurrentScopeInstance(), cloneVariablesDeclarees());
    }

    public ScopeInstance makeScopeInstanceFromScopeParent() {
        return new ScopeInstance(parent, cloneVariablesDeclarees());
    }

    /**
     * Cette fonction devrait être appelée <b>SEULEMENT</b> au compile time
     *
     * @param variable la variable qui est déclarée
     */
    public void declareVariable(ASCVariable variable) {
        variablesDeclarees.push(variable);
    }

    public ASCVariable getVariable(String nom) {
        return variablesDeclarees.stream()
                .filter(var -> var.getValue().equals(nom))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Scope{" +
                "variablesDeclarees=" + variablesDeclarees +
                '}';
    }

    //#endregion


    //#region --------- ScopeInstance ---------

    public static class ScopeInstance {
        private final ScopeInstance parent;
        private final Stack<ASCVariable> variableStack;

        private ScopeInstance(ScopeInstance parent, Stack<ASCVariable> variableStack) {
            this.parent = parent;
            this.variableStack = variableStack;
        }

        public ScopeInstance getParent() {
            return parent;
        }

        public Stack<ASCVariable> getVariableStack() {
            return variableStack;
        }

        public ASCVariable getVariable(String nom) {
            return variableStack.stream()
                    .filter(var -> var.getName().equals(nom))
                    .findFirst()
                    .orElse(parent == null ? null : parent.getVariable(nom));
        }

        public ASCVariable getVariable(ASCVariable variable) {
            return variableStack.stream()
                    .filter(var -> var.equals(variable))
                    .findFirst()
                    .orElse(parent == null ? null : parent.getVariable(variable));
        }

        @Override
        public String toString() {
            return "ScopeInstance{" +
                    "variableStack=" + variableStack +
                    ", parent=" + parent +
                    '}';
        }
    }

    //#endregion

}

























