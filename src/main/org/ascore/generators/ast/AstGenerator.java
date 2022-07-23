package org.ascore.generators.ast;

import org.ascore.errors.ASCErrors;
import org.ascore.ast.AstNode;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.generators.lexer.LexerGenerator;
import org.ascore.tokens.Token;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AstGenerator<AstFrameKind extends Enum<?>> {
    private final Stack<AstFrameKind> astFrameStack = new Stack<>();  // for compile time selection of ast frames
    private final Hashtable<AstFrameKind, AstFrame> astFrameTable = new Hashtable<>();  // to store the different ast frames
    private AstFrame currentAstFrame;

    public static void hasSafeSyntax(Token[] expressionArray) {
        int parentheses = 0;
        int braces = 0;
        int crochets = 0;

        for (Token token : expressionArray) {
            switch (token.getName()) {
                case "PARENT_OUV" -> parentheses++;
                case "PARENT_FERM" -> parentheses--;

                case "CROCHET_OUV" -> crochets++;
                case "CROCHET_FERM" -> crochets--;

                case "BRACES_OUV" -> braces++;
                case "BRACES_FERM" -> braces--;
            }
        }

        String pluriel = Math.abs(parentheses) > 1 ? "s" : "";

        //System.out.println(Arrays.toString(expressionArray));

        switch (Integer.compare(parentheses, 0)) {
            case -1 -> throw new ASCErrors.ErreurSyntaxe(-parentheses + " parenth\u00E8se" + pluriel + " ouvrante" + pluriel + " '(' manquante" + pluriel);
            case 1 -> throw new ASCErrors.ErreurSyntaxe(parentheses + " parenth\u00E8se" + pluriel + " fermante" + pluriel + " ')' manquante" + pluriel);
        }

        pluriel = Math.abs(braces) > 1 ? "s" : "";
        switch (Integer.compare(braces, 0)) {
            case -1 -> throw new ASCErrors.ErreurSyntaxe(-braces + " accolade" + pluriel + " ouvrante" + pluriel + " '{' manquante" + pluriel);
            case 1 -> throw new ASCErrors.ErreurSyntaxe(braces + " accolade" + pluriel + " fermante" + pluriel + " '}' manquante" + pluriel);
        }

        pluriel = Math.abs(crochets) > 1 ? "s" : "";
        switch (Integer.compare(crochets, 0)) {
            case -1 -> throw new ASCErrors.ErreurSyntaxe(-crochets + " crochet" + pluriel + " ouvrant" + pluriel + " '[' manquant" + pluriel);
            case 1 -> throw new ASCErrors.ErreurSyntaxe(crochets + " crochet" + pluriel + " fermant" + pluriel + " ']' manquant" + pluriel);
        }

    }

    public static Matcher sameStructureStatement(String line, String structurePotentielle) {
        //System.out.println(structurePotentielle.replaceAll("( ?)(#?)expression ?", Matcher.quoteReplacement("\\b.+")));
        //Pattern structurePattern = Pattern.compile(structurePotentielle.replaceAll("( ?)(#?)expression ?", Matcher.quoteReplacement("\\b.+")));
        // FIXME Maybe a catastrophic change idk
        Pattern structurePattern = Pattern.compile(structurePotentielle.replaceAll("( ?)(#?)expression ?", Matcher.quoteReplacement("\\b *([A-Z_] ?)+ *")));

        return structurePattern.matcher(line);
    }

    // TODO TEST!!!!!!
    public static Matcher sameStructureExpression(String line, String structurePotentielle) {
        Pattern structurePattern = Pattern.compile(structurePotentielle
                .replaceAll("#expression", Matcher.quoteReplacement("\\b.+"))
                .replaceAll("!expression *", Matcher.quoteReplacement("(?<!expression )"))
        );
        //System.out.println(line + " matcher:" + structurePattern.matcher(line));
        return structurePattern.matcher(line);
    }

    protected AstFrame currentAstFrame() {
        return currentAstFrame;
    }

    protected ArrayList<String> currentExpressionsOrder() {
        return currentAstFrame.expressionsOrder();
    }

    protected Hashtable<String, AstNode<? extends Expression<?>>> currentExpressionsDict() {
        return currentAstFrame.expressionsDict();
    }

    protected ArrayList<String> currentStatementsOrder() {
        return currentAstFrame.statementsOrder();
    }

    protected Hashtable<String, AstNode<? extends Statement>> currentStatementsDict() {
        return currentAstFrame.statementsDict();
    }

    protected void pushAstFrame(AstFrameKind astFrameKind) {
        astFrameStack.push(astFrameKind);
        setCurrentAstFrame(astFrameKind);
    }

    protected void popAstFrame() {
        astFrameStack.pop();
        if (astFrameStack.isEmpty()) {
            currentAstFrame = null;
        } else {
            setCurrentAstFrame(astFrameStack.peek());
        }
    }

    protected void defineAstFrame(AstFrameKind kind) {
        setCurrentAstFrame(kind);
    }

    private void setCurrentAstFrame(AstFrameKind kind) {
        astFrameTable.putIfAbsent(kind, new AstFrame());
        currentAstFrame = astFrameTable.get(kind);
    }

    private ArrayList<String> addSubAstOrder(Hashtable<String, AstNode<? extends Expression<?>>> sous_ast) {
        ArrayList<String> nouvelOrdre = new ArrayList<>(currentExpressionsOrder());

        if (sous_ast.size() > 0) {
            for (String pattern : sous_ast.keySet()) {
                pattern = LexerGenerator.remplaceCategoriesByMembers(pattern);
                int importance = sous_ast.get(pattern).getImportance();
                if (importance == -2) continue; // -2 = take the same place as the replaced expression

                nouvelOrdre.remove(pattern);
                if (importance == -1) {
                    nouvelOrdre.add(pattern);
                } else {
                    if (nouvelOrdre.size() > importance && nouvelOrdre.get(importance) == null) {
                        nouvelOrdre.set(importance, pattern);
                    } else {
                        if (nouvelOrdre.size() < importance) nouvelOrdre.add(pattern);
                        else nouvelOrdre.add(importance, pattern);
                    }
                }
            }
            nouvelOrdre.removeIf(Objects::isNull);
            //System.out.println(this.ordreExpressions);
        }
        return nouvelOrdre;
    }

    public Expression<?> evalOneExpr(ArrayList<Object> expressions, Hashtable<String, AstNode<? extends Expression<?>>> sous_ast) {
        var result = eval(expressions, sous_ast);
        if (result.size() != 1) {
            throw new ASCErrors.ErreurSyntaxe("Erreur ligne 106 dans AstGenerator");
        } else {
            return result.get(0);
        }
    }

    public ArrayList<Expression<?>> eval(ArrayList<Object> expressions, Hashtable<String, AstNode<? extends Expression<?>>> sous_ast) {

        var regleSyntaxeDispo = new Hashtable<>(currentExpressionsDict());
        var ordreRegleSyntaxe = new ArrayList<>(currentExpressionsOrder());

        if (sous_ast != null) {
            regleSyntaxeDispo.putAll(sous_ast);
            ordreRegleSyntaxe = addSubAstOrder(sous_ast);
        }

        //System.out.println(expressions);
        if (expressions.size() == 0) {
            return new ArrayList<>();
        }
        // if the first expression is an arraylist, all the expressions are array lists
        if (expressions.get(0) instanceof ArrayList<?>) {
            ArrayList<Object> expressionList = new ArrayList<>();
            for (Object expr : expressions) {
                expressionList.addAll(eval((ArrayList<Object>) expr, regleSyntaxeDispo));
            }
            return expressionList.stream().map(e -> (Expression<?>) e).collect(Collectors.toCollection(ArrayList::new));
        }

        ArrayList<Object> expressionArray = new ArrayList<>(expressions);

        hasSafeSyntax(expressionArray.stream().filter(e -> e instanceof Token).toArray(Token[]::new));

        for (String regleSyntaxeEtVariante : ordreRegleSyntaxe) {
            String[] split = regleSyntaxeEtVariante.split("~");
            for (int idxVariante = 0; idxVariante < split.length; idxVariante++) {
                String regleSyntaxe = split[idxVariante];
                regleSyntaxe = regleSyntaxe.trim();

                List<String> membresRegleSyntaxe = Arrays.asList(regleSyntaxe.split(" "));
                int nbNotExpr = (int) membresRegleSyntaxe.stream().filter(e -> e.equals("!expression")).count();
                int longueurRegleSyntaxe = membresRegleSyntaxe.size() - nbNotExpr;

                int i = 0, debut, exprLength;
                while (i + longueurRegleSyntaxe <= expressionArray.size()) {

                    List<String> expressionNom = new ArrayList<>();

                    for (Object expr : expressionArray) {
                        expressionNom.add(expr instanceof Token token ? token.getName() : "expression");
                    }
                    Matcher match = sameStructureExpression(String.join(" ", expressionNom.subList(i, expressionNom.size())), regleSyntaxe);
                    if (regleSyntaxe.contains("#expression") && match.find()) {
                        if (match.start() != 0 || (expressionArray.get(i) instanceof Token && regleSyntaxe.startsWith("expression"))) {
                            i++;
                            continue;
                        }
                        debut = i;
                        expressionNom = expressionNom.subList(debut, expressionNom.size());
                        int fin = debut + expressionNom.size();

                        List<Object> expr = expressionArray.subList(debut, fin);

                        Expression<?> capsule = regleSyntaxeDispo
                                .get(regleSyntaxeEtVariante)
                                .apply(new ArrayList<>(expr), idxVariante);

                        ArrayList<Object> newArray = new ArrayList<>(expressionArray.subList(0, debut));
                        newArray.add(capsule);
                        newArray.addAll(expressionArray.subList(fin, expressionArray.size()));

                        expressionArray = newArray;

                    } else {
                        if (regleSyntaxe.contains("!expression") && i > 0 && expressionNom.get(i - 1).equals("expression")) {
                            i++;
                            continue;
                        }
                        debut = i;
                        exprLength = debut + longueurRegleSyntaxe;

                        if (sameStructureExpression(String.join(" ", expressionNom.subList(debut, exprLength)), regleSyntaxe).matches()) {
                            if ((regleSyntaxe.startsWith("expression") &&
                                 (!(expressionArray.get(debut) instanceof Expression<?>))
                                 ||
                                 expressionArray.get(debut) == null)
                            ) {
                                i++;
                                continue;
                            }
                            Expression<?> capsule = regleSyntaxeDispo
                                    .get(regleSyntaxeEtVariante)
                                    .apply(expressionArray.subList(debut, exprLength), idxVariante);

                            ArrayList<Object> newArray = new ArrayList<>(debut != 0 ? expressionArray.subList(0, debut) : new ArrayList<>());
                            newArray.add(capsule);
                            newArray.addAll(expressionArray.subList(debut + longueurRegleSyntaxe, expressionArray.size()));

                            expressionArray = newArray;
                            if (longueurRegleSyntaxe == 1) i++;

                        } else {
                            i++;
                        }
                    }
                }
            }
        }

        Token[] token = expressionArray.stream().filter(e -> e instanceof Token).toArray(Token[]::new);

        if (token.length > 0) {
            throw new ASCErrors.ErreurSyntaxe("Expression ill\u00E9gale: " + String.join(" ", Arrays.stream(token).map(Token::getValue).toArray(String[]::new)));
        }

        //System.out.println(expressionArray);
        return ((ArrayList<?>) expressionArray).stream().map(e -> (Expression<?>) e).collect(Collectors.toCollection(ArrayList::new));
    }

    protected void reset() {
        astFrameTable.clear();
        astFrameStack.clear();
    }

    protected void addStatement(String pattern, AstFrameKind frameKind) throws NoSuchElementException {
        pattern = LexerGenerator.remplaceCategoriesByMembers(pattern);
        var fonction = astFrameTable.get(frameKind).statementsDict().get(pattern);
        if (fonction == null) {
            throw new NoSuchElementException("Programme non trouv\u00E9 dans la frame " + frameKind + ": " + pattern);
        }
        currentAstFrame().addStatement(pattern, fonction);
    }

    protected void addStatement(String pattern, AstNode<? extends Statement> fonction) {
        currentAstFrame().addStatement(pattern, fonction);
    }

    protected void addStatement(String pattern, Function<List<Object>, ? extends Statement> fonction) {
        currentAstFrame().addStatement(pattern, fonction);
    }

    protected void addStatement(String pattern, BiFunction<List<Object>, Integer, ? extends Statement> fonction) {
        currentAstFrame().addStatement(pattern, fonction);
    }

    protected void addExpression(String pattern, AstFrameKind frameKind) throws NoSuchElementException {
        pattern = LexerGenerator.remplaceCategoriesByMembers(pattern);
        var fonction = astFrameTable.get(frameKind).expressionsDict().get(pattern);
        if (fonction == null) {
            throw new NoSuchElementException("Expression non trouv\u00E9e dans la frame " + frameKind + ": " + pattern);
        }
        currentAstFrame().addExpression(pattern, fonction);
    }

    protected void addExpression(String pattern, AstNode<? extends Expression<?>> fonction) {
        currentAstFrame().addExpression(pattern, fonction);
    }

    protected void addExpression(String pattern, Function<List<Object>, ? extends Expression<?>> fonction) {
        currentAstFrame().addExpression(pattern, fonction);
    }

    protected void addExpression(String pattern, BiFunction<List<Object>, Integer, ? extends Expression<?>> fonction) {
        currentAstFrame().addExpression(pattern, fonction);
    }

    protected void setOrdreProgramme() {
        var programmesDict = currentStatementsDict();
        var ordreProgrammes = currentStatementsOrder();
        for (int i = 0; i < programmesDict.size(); ++i) {
            ordreProgrammes.add(null);
        }
        for (String pattern : programmesDict.keySet()) {
            int importance = programmesDict.get(pattern).getImportance();
            if (importance == -1) {
                ordreProgrammes.add(pattern);
            } else {
                //if (ordreProgrammes.get(importance) == null) {
                //    ordreProgrammes.set(importance, pattern);
                //} else {
                //    ordreProgrammes.add(importance, pattern);
                //}
                ordreProgrammes.add(importance, pattern);
            }
        }
        ordreProgrammes.removeIf(Objects::isNull);
        //System.out.println(this.ordreProgrammes);
    }

    protected void setOrdreExpression() {
        var expressionsDict = currentExpressionsDict();
        var ordreExpressions = currentExpressionsOrder();
        for (int i = 0; i < expressionsDict.size(); ++i) {
            ordreExpressions.add(null);
        }
        for (String pattern : expressionsDict.keySet()) {
            int importance = expressionsDict.get(pattern).getImportance();
            if (importance == -1) {
                ordreExpressions.add(pattern);
            } else {
                if (ordreExpressions.get(importance) == null) {
                    ordreExpressions.set(importance, pattern);
                } else {
                    ordreExpressions.add(importance, pattern);
                }
            }
        }
        ordreExpressions.removeIf(Objects::isNull);
        //System.out.println(this.ordreExpressions);
    }

    public Statement parse(List<Token> listToken) {
        var programmesDict = currentStatementsDict();

        var programmeEtIdxVariante = getStatementOrThrow(listToken);
        int idxVariante = programmeEtIdxVariante.getKey();
        String programmeEtVariante = programmeEtIdxVariante.getValue();
        String programme = programmeEtVariante.split("~")[idxVariante];

        var expressions_programme = getDivisionExpressionsStatement(listToken, programmeEtVariante, idxVariante);

        var expressions = expressions_programme.getValue();
        var programmeToken = expressions_programme.getKey();


        var arbre = eval(
                expressions.stream().map(e -> (Object) e).collect(Collectors.toCollection(ArrayList::new)),
                programmesDict.get(programmeEtVariante).getSousAst()
        );

        ArrayList<Object> finalLine = new ArrayList<>(Arrays.asList(programme.split(" ")));


        Iterator<?> expressionIt = arbre.iterator();
        Iterator<Token> programmeIt = programmeToken.iterator();

        finalLine.replaceAll(e -> e.equals("expression") ? expressionIt.hasNext() ? expressionIt.next() : null : programmeIt.hasNext() ? programmeIt.next() : null);

        //System.out.println(finalLine);
        if (expressionIt.hasNext()) {
            throw new ASCErrors.ErreurSyntaxe("Syntaxe invalide. Est-ce qu'il manque une virgule entre deux \u00E9l\u00E9ments?");
        }

        return programmesDict
                .get(programmeEtVariante)
                .apply(finalLine, idxVariante);
    }

    /**
     * @param listToken the list of token making the lexed line
     * @return an entry composed of the variant idx as the key and the programme as the value
     * @throws ASCErrors.ErreurSyntaxe if there are no programme that match the tokens in listToken
     */
    public Map.Entry<Integer, String> getStatementOrThrow(List<Token> listToken) {
        var ordreProgrammes = currentStatementsOrder();

        String programmeTrouve = null;
        List<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> structureLine.add(e.getName()));
        int idxVariante = 0;

        int nbTokenProgrammeTrouvee = 0;
        for (String programme : ordreProgrammes) {
            //System.out.println(programme + " " + structureLine);
            String[] variantes = programme.split("~");
            for (int i = 0; i < variantes.length; i++) {
                String variante = variantes[i];
                if (sameStructureStatement(String.join(" ", structureLine), variante).matches()) {
                    int nbTokenProgrammeAlter = variante.replaceAll("#?expression", "").replaceAll("(\\(.+\\))|(\\w+)", "T").length();
                    if (programmeTrouve == null || nbTokenProgrammeTrouvee < nbTokenProgrammeAlter) {
                        programmeTrouve = programme;
                        idxVariante = i;
                        nbTokenProgrammeTrouvee = nbTokenProgrammeAlter;
                    }
                }
            }
        }
        if (programmeTrouve == null) {
            throw new ASCErrors.ErreurSyntaxe("Syntaxe invalide: " + listToken
                    .stream()
                    .map(Token::getValue)
                    .toList()
            );
        }
        return Map.entry(idxVariante, programmeTrouve);
    }

    /**
     * @param listToken
     * @param programme
     * @param idxVariante
     * @return map: key=programme, value=expressions
     */
    private Map.Entry<ArrayList<Token>, ArrayList<ArrayList<Token>>> getDivisionExpressionsStatement(List<Token> listToken, String programme, Integer idxVariante) {
        programme = programme.split("~")[idxVariante];
        ArrayList<String> structureLine = new ArrayList<>();
        listToken.forEach(e -> structureLine.add(e.getName()));

        ArrayList<String> structureProgramme = new ArrayList<>(Arrays.asList(programme.split(" ")));
        // structureProgramme.removeIf(e -> e.equals("expression") || e.equals("#expression"));
        Iterator<String> iterProgramme = structureProgramme.iterator();

        ArrayList<ArrayList<Token>> expressionsList = new ArrayList<>();
        ArrayList<Token> programmeList = new ArrayList<>();

        if (programme.contains("expression") || programme.contains("#expression")) {
            String clef = iterProgramme.hasNext() ? iterProgramme.next() : "";

            ArrayList<Token> expressionList = new ArrayList<>();

            for (int i = 0; i < structureLine.size(); ++i) {
                // FIXME Maybe a catastrophic change idk part 2
                if (clef.equals("expression") || clef.equals("#expression")) {
                    expressionList.add(listToken.get(i));
                    clef = iterProgramme.hasNext() ? iterProgramme.next() : "";
                    continue;
                }
                if (!clef.isBlank() && structureLine.get(i).matches(clef)) {
                    clef = iterProgramme.hasNext() ? iterProgramme.next() : "";

                    programmeList.add(listToken.get(i));
                    expressionsList.add(expressionList);
                    expressionList = new ArrayList<>();
                } else {
                    expressionList.add(listToken.get(i));
                }
            }
            expressionsList.add(expressionList);
            expressionsList.removeIf(ArrayList::isEmpty);
        } else {
            programmeList = new ArrayList<>(listToken);
        }
        return Map.entry(programmeList, expressionsList);
    }
}

























