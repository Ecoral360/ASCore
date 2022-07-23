package org.ascore.executor;

import io.github.cdimascio.dotenv.Dotenv;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.errors.ASCErrors.*;
import org.ascore.generators.ast.AstGenerator;
import org.ascore.generators.lexer.LexerGenerator;
import org.ascore.lang.ASCLexer;
import org.ascore.lang.ASCParser;
import org.ascore.managers.data.Data;
import org.ascore.utils.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Stack;



/*

afficher 3

attendre 2

var = 2

si var == 2
	avancer
sinon
	afficher faux
fin si

afficher "fin"

*/


/**
 * Cette classe est responsable de la compilation et de l'ex\u00E9cution du langage
 *
 * @author Mathis Laroche
 */
public class ASCExecutor<ExecutorState extends ASCExecutorState> {

    private final static int MAX_DATA_BEFORE_SEND;
    // coordonne ou commencer tous les statements
    final private static Coordinate debutCoord = new Coordinate("<0>main");

    static {
        if (new File("./.env").exists()) {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./.env")
                    .load();
            MAX_DATA_BEFORE_SEND = Integer.parseInt(dotenv.get("MAX_DATA_BEFORE_SEND"));
        } else {
            MAX_DATA_BEFORE_SEND = 500;
        }
    }

    // state
    private final ExecutorState executorState;

    // lexer et parser
    private final LexerGenerator lexer;
    private final ASCPrecompiler preCompiler;

    //------------------------ compilation -----------------------------//
    private final Hashtable<String, Hashtable<String, Statement>> coordCompileDict = new Hashtable<>();
    private final ArrayList<Coordinate> coordCompileTime = new ArrayList<>();
    // Coordonnee utilisee lors de l'execution pour savoir quelle ligne executer
    private final Coordinate coordRunTime = new Coordinate(debutCoord.toString());

    // data explaining the actions to do to the com.server
    private final ArrayList<Data> datas = new ArrayList<>();
    // data stack used when the program asks the site for information
    private final Stack<Object> dataResponse = new Stack<>();
    //debug mode
    public boolean debug = false;
    // ast
    private AstGenerator<?> parser;
    private JSONObject context = null;
    private String anciennesLignes = null;
    // failsafe
    private boolean compilationActive = false;
    private boolean executionActive = false;
    private boolean canExecute = false;


    public ASCExecutor() {
        this(null, null, null, null);
    }

    @SuppressWarnings("unchecked")
    public ASCExecutor(
            LexerGenerator lexer,
            AstGenerator<?> parser,
            ASCPrecompiler preCompiler,
            ExecutorState executorState
    ) {
        this.lexer = lexer != null ? lexer : new ASCLexer();
        this.parser = parser != null ? parser : new ASCParser(this);
        this.preCompiler = preCompiler != null ? preCompiler : new PreCompiler();
        this.executorState = executorState != null ? executorState : (ExecutorState) new ASCExecutorState();
    }

    public static void main(String[] args) {

        String lines = """
                show 3 + 99
                """;


        //Analyste analyste = new Analyste(lines);
        //analyste.afficherProgramme();
        //analyste.analyserLexing(Analyste.Precision.TOUT_EN_MEME_TEMPS, true);

        ASCExecutor<?> executor = new ASCExecutor<>();
        executor.debug = true;
        Object a;
        if (!(a = executor.compiler(lines, true)).equals("[]")) System.out.println(a);
        // executeur.printCompileDict();
        System.out.println(executor.executerMain(false));
    }

    public static void printCompiledCode(String code) {
        int nbTab = 0;
        code = code.replaceAll(", ", ",");

        boolean inString = false;
        boolean skipNext = false;
        for (String chr : code.split("")) {
            if (inString) {
                System.out.print(chr);
                if (chr.equals("\\")) {
                    skipNext = true;
                    continue;
                }
                inString = !chr.equals("'") || skipNext;
                skipNext = false;
                continue;
            }
            switch (chr) {
                case "{", "[" -> {
                    nbTab++;
                    System.out.print(" " + chr + "\n" + "\t".repeat(nbTab));
                }
                case "}", "]" -> {
                    nbTab--;
                    System.out.print("\n" + "\t".repeat(nbTab) + chr);
                }
                case "," -> System.out.print(chr + "\n" + "\t".repeat(nbTab));
                case "'" -> {
                    System.out.print(chr);
                    inString = true;
                }
                default -> System.out.print(chr);
            }
        }
        System.out.println();
    }

    /**
     * @return le lexer utilise par l'interpreteur (voir ASLexer)
     */
    public LexerGenerator getLexer() {
        return lexer;
    }

    public ExecutorState getExecutorState() {
        return executorState;
    }

    // methode utilisee a chaque fois qu'une info doit etre afficher par le langage
    public void ecrire(String texte) {
        if (debug) System.out.println(texte);
    }

    public void printCompileDict() {
        int nbTab = 0;
        for (String scope : coordCompileDict.keySet()) {
            System.out.print("\n" + scope + ":\n");
            String[] ordreCoord = coordCompileDict.get(scope).keySet()
                    .stream()
                    .sorted(Comparator.comparingInt(coord -> coordCompileDict.get(scope).get(coord).getNumLine()))
                    .toArray(String[]::new);

            for (String coord : ordreCoord) {
                String prog = coordCompileDict.get(scope).getOrDefault(coord,
                        new Statement() {
                            @Override
                            public Object execute() {
                                return null;
                            }
                        }
                ).toString();
                System.out.print("\t".repeat(nbTab) + coord + "=");
                printCompiledCode(prog);
            }
        }
    }

    public void addData(Data data) {
        datas.add(data);
    }

    public Stack<Object> getDataResponse() {
        return this.dataResponse;
    }

    public Object getDataResponseOrAsk(String dataName, Object... additionnalParams) {
        if (this.dataResponse.isEmpty()) {
            Data dataToGet = new Data(Data.Id.GET).addParam(dataName);
            for (var param : additionnalParams)
                dataToGet.addParam(param);
            throw new ASCErrors.StopGetInfo(dataToGet);
        } else
            return this.dataResponse.pop();
    }

    public JSONObject getContext() {
        if (context == null)
            throw new ASCErrors.ErreurContexteAbsent("Il n'y a pas de contexte");
        return context;
    }

    public void setContext(JSONObject context) {
        if (this.context != null) {
            throw new IllegalArgumentException("aaaaa");
        }
        this.context = context;
    }

    public Object pushDataResponse(Object item) {
        return this.dataResponse.push(item);
    }

    /**
     * @return les dernieres lignes a avoir ete compile sous la forme d'une array de String
     */
    public String getLignes() {
        return anciennesLignes;
    }

    /**
     * @param coord <br><li>la coordonne d'une certaine ligne de code</li>
     * @return la position de la la ligne de code dans le code
     */
    public Integer getLineFromCoord(Coordinate coord) {
        return coordCompileDict.get(coord.getScope()).get(coord.toString()).getNumLine();
    }

    /**
     * @return le dictionnaire de coordonnees compilees
     */
    public Hashtable<String, Hashtable<String, Statement>> obtenirCoordCompileDict() {
        return coordCompileDict;
    }

    public boolean enAction() {
        return (compilationActive || executionActive);
    }

    public void arreterExecution() {
        executionActive = false;
    }

    /**
     * @return le parser utilise par l'interpreteur (voir ASAst)
     */
    public AstGenerator<?> getParser() {
        return parser;
    }

    public void setParser(AstGenerator<?> parser) {
        this.parser = parser;
    }

    /**
     * @param nomDuScope <br><li>cree un nouveau scope et ajoute la premiere coordonnee a ce scope</li>
     * @return la premiere coordonnee du scope
     */
    public String nouveauScope(String nomDuScope) {
        coordCompileDict.put(nomDuScope, new Hashtable<>());
        // peut-etre faire en sorte qu'il y ait une erreur si le scope existe deja
        coordCompileTime.add(new Coordinate("<0>" + nomDuScope));
        coordCompileDict.get(nomDuScope).put("<0>" + nomDuScope, new Statement() {
            @Override
            public Object execute() {
                return null;
            }

            @Override
            public String toString() {
                return "DEBUT FONCTION: '" + nomDuScope + "'";
            }
        });
        return "<0>" + nomDuScope;
    }

    /**
     * met fin au scope actuel et retourne dans le scope precedent
     *
     * @return la nouvelle coordonne actuelle
     */
    public String finScope() {
        if (coordCompileTime.size() == 1) {
            throw new ErreurFermeture("main", "");
        }
        coordCompileTime.remove(coordCompileTime.size() - 1);
        return coordCompileTime.get(coordCompileTime.size() - 1).toString();
    }

    /**
     * @return la coordonne actuelle lors de l'execution du code
     */
    public Coordinate obtenirCoordRunTime() {
        return coordRunTime;
    }

    /**
     * permet de changer la coordonne lors de l'execution du code
     *
     * @param coord <br><li>la nouvelle coordonnee</li>
     */
    public void setCoordRunTime(String coord) {
        coordRunTime.setCoord(coord);
    }

    /**
     * @param nom
     * @return
     */
    public boolean leBlocExiste(String nom) {
        return coordCompileDict.get(coordRunTime.getScope()).containsKey("<1>" + nom + coordRunTime);
    }

    public boolean laCoordExiste(String coord) {
        return coordCompileDict.get(coordRunTime.getScope()).containsKey(coord + coordRunTime);
    }

    /**
     * Cette fonction permet de compiler des lignes de code afin de pouvoir les executer (voir Executeur.executerMain)
     *
     * @param lignes            <li>
     *                          Type: String[]
     *                          </li>
     *                          <li>
     *                          Represente les lignes de code a compiler, une ligne se finit par un <code>\n</code>
     *                          </li>
     * @param compilationForcee <br><li>
     *                          Type: boolean
     *                          </li>
     *                          <li>
     *                          Indique si l'on souhaite forcer la compilation du code, <br>
     *                          (le code sera alors compile meme s'il est identique au code precedemment compile)
     *                          </li>
     */
    public JSONArray compiler(String lignes, boolean compilationForcee) {
        reset();

        /*
         * Cette condition est remplie si l'array de lignes de codes mises en parametres est identique
         * a l'array des dernieres lignes de code compilees
         *
         * -> Elle evite donc de compiler le meme code plusieurs fois
         *
         * Cependant, cette condition peut etre overwrite si la compilation est forcee (compilationForce serait alors true)
         */
        if (lignes.equals(anciennesLignes) && !compilationForcee) {
            if (debug) System.out.println("No changes: compilation done");
            return new JSONArray();
        } else {
            // Si le code est different ou que la compilation est forcee, compiler les lignes
            //System.out.println(Arrays.toString(PreCompiler.preCompile(lignes)));
            lignes = preCompiler.preCompile(lignes);
            return compiler(lignes);
        }
    }

    /**
     * Fonction privee charge de compiler un array de ligne de code
     *
     * @param lignes <li>
     *               Type: String[]
     *               </li>
     *               <li>
     *               Represente les lignes de code a compiler, une ligne se finit par un <code>\n</code>
     *               </li>
     */
    private JSONArray compiler(String lignes) {

        // sert au calcul du temps qu'a pris le code pour etre compile
        LocalDateTime before = LocalDateTime.now();

        if (debug) System.out.println("compiling...");

        // vide le dictionnaire de coordonne ainsi que la liste de coordonne
        coordCompileDict.clear();
        coordCompileTime.clear();

        // indique le programme est en train de compiler le code
        compilationActive = true;
        canExecute = false;

        coordCompileTime.add(debutCoord);
        /*
         *  ajoute le scope "main" au dictionnaire de coordonnee
         *  c'est dans ce sous-dictionnaire que seront mises toutes les lignes appartenant au scope "main"
         */
        coordCompileDict.put("main", new Hashtable<>());

        var codeTokenized = lexer.splitInStatements(lexer.lex(lignes));

        // boucle a travers toutes les lignes de l'array "lignes"
        for (int i = 0; i < codeTokenized.size(); i++) {
            var lineToken = codeTokenized.get(i);

            // produit la liste de Token representant la ligne (voir lexer.lex)


            // obtiens la coordonne ainsi que le scope ou sera enregistree la ligne compilee
            String coordActuelle = coordCompileTime.get(coordCompileTime.size() - 1).toString();
            String scopeActuel = coordCompileTime.get(coordCompileTime.size() - 1).getScope();

            // System.out.println(coordActuelle + "   " + scopeActuel);
            try {
                /*
                 * transforme la liste de Token obtenu precedemment en un Object[] de forme:
                 * Object[] {
                 * 		programme (String representant le programme de la ligne
                 * 					ex: AFFICHER expression, POUR NOM_VARIABLE DANS expression, etc.)
                 *
                 * 		arbre de syntaxe de la ligne (voir parser.parse)
                 *
                 * 		le programme sous la forme d'une liste de Token
                 * }
                 */

                Statement ligneParsed;

                ligneParsed = parser.parse(lineToken);

                ligneParsed.setNumLine(i + 1);

                // met ligneParsed dans le dictionnaire de coordonne
                coordCompileDict.get(scopeActuel).put(coordActuelle, ligneParsed);

                // accede a la fonction prochaineCoord du programme trouvee afin de definir la prochaine coordonnee
                coordRunTime.setCoord(ligneParsed.getNextCoordinate(new Coordinate(coordActuelle), lineToken).toString());
                coordActuelle = coordRunTime.toString();
                scopeActuel = coordRunTime.getScope();

                // si la coordonnee est de taille 0, cela signifie que le code contient un "fin ..." a l'exterieur d'un bloc de code
                // -> cela provoque une erreur de fermeture
                if (coordActuelle.length() == 0) {
                    throw new ErreurFermeture(scopeActuel);
                }

            } catch (ASCErrors.ASCError err) {
                canExecute = false;
                compilationActive = false;
                err.afficher(this, i + 1);
                return new JSONArray().put(err.getAsData(i + 1));

            }

            // update la coordonnee
            coordRunTime.plusUn();
            coordCompileTime.set(
                    coordCompileTime.size() - 1,
                    new Coordinate(coordRunTime.toString())
            );
        }

        // ajoute une ligne null à la fin pour signaler la fin de l'exécution
        Statement fin = new Statement.EndOfProgramStatement();
        fin.setNumLine(codeTokenized.size() + 1);
        coordCompileDict.get("main").put(coordRunTime.toString(), fin);

        try {
            if (!coordRunTime.getBlocActuel().equals("main")) {
                throw new ErreurFermeture(coordRunTime.getBlocActuel());
            }
//            if (!ASFonctionManager.obtenirStructure().isBlank()) {
//                throw new ErreurFermeture(ASFonctionManager.obtenirStructure());
//            }
        } catch (ASCErrors.ASCError err) {
            canExecute = false;
            compilationActive = false;
            err.afficher(this, codeTokenized.size());
            return new JSONArray().put(err.getAsData(codeTokenized.size()));

        }

        /*
         * affiche le temps qu'a pris la compilation du programme
         */
        if (debug)
            System.out.println("compilation done in "
                               + (LocalDateTime.now().toLocalTime().toNanoOfDay() - before.toLocalTime().toNanoOfDay()) / Math.pow(10, 9)
                               + " seconds\n");

        // set la valeur des anciennes lignes de code aux nouvelles lignes donnees en parametre
        anciennesLignes = lignes;
        compilationActive = false;
        canExecute = true;
        return new JSONArray();
    }

    public Object executerScope(String scope, Hashtable<String, Hashtable<String, Statement>> coordCompileDict, String startCoord) {
        if (coordCompileDict == null) coordCompileDict = this.coordCompileDict;
        if (startCoord == null) startCoord = "<0>" + scope;

        // set la coordonne au debut du scope
        coordRunTime.setCoord(startCoord);

        Object result = "[]";
        Statement statement = null;

        while (executionActive && canExecute) {
            // System.out.println(coordRunTime);
            // get la ligne a executer dans le dictionnaire de coordonnees
            statement = coordCompileDict.get(scope).get(coordRunTime.toString());

            if (statement instanceof Statement.EndOfProgramStatement) { // ne sera vrai que si cela est la derniere ligne du programme
                coordRunTime.setCoord(null);
                break;
            }

//            var resultPair = executeStatement(statement);
//            if (resultPair.first() == null) { // if the first element is null, it means that to return the second element as is
//                return resultPair.second();
//            } else if (resultPair.first()) { // if the first element is true, it means that to set the result as the second element and continue
//                result = resultPair.second();
//            } else {                         // if the first element is false, it means that to set the result as the second element and break
//                result = resultPair.second();
//                break;
//            }


            // s'il y a une erreur dans l'execution, on arr�te l'execution et on �crit le message d'erreur dans la console de l'app
            try {
                // execution de la ligne et enregistrement du resultat dans la variable du meme nom
                result = statement.execute();

                if (result instanceof Data) {
                    datas.add((Data) result);

                } else if (result != null && !coordRunTime.getScope().equals("main")) {
                    // ne sera vrai que si l'on retourne d'une fonction
                    break;
                }

                if (datas.size() >= MAX_DATA_BEFORE_SEND) {
                    synchronized (datas) {
                        return datas.toString();
                    }
                }
            } catch (StopSendData e) {
                return e.getDataString();

            } catch (StopGetInfo e) {
                datas.add(e.getData());
                return datas.toString();

            } catch (StopSetInfo e) {
                datas.add(e.getData());

            } catch (ASCErrors.ASCError e) {
                // si l'erreur lancee est de type ASErreur.ErreurExecution (Voir ASErreur.java),
                // on l'affiche et on arrete l'execution du programme
                datas.add(e.getAsData(this));
                arreterExecution();
                e.afficher(this);
                result = null;
                break;

            } catch (RuntimeException e) {
                // s'il y a une erreur, mais que ce n'est pas une erreur se trouvant dans ASErreur, c'est une
                // erreur de syntaxe, comme l'autre type d'erreur, on l'affiche et on arrete l'execution du programme
                e.printStackTrace();
                datas.add(new ErreurSyntaxe("Une erreur interne inconnue est survenue lors de l'ex\u00E9cution de la ligne, v\u00E9rifiez que la syntaxe est valide")
                        .getAsData(this));
                if (debug) System.out.println(coordRunTime);
                arreterExecution();
                result = null;
                break;
            }
            // on passe a la coordonnee suivante
            coordRunTime.plusUn();
        }
        return (statement instanceof Statement.EndOfProgramStatement || !executionActive || result == null) ? datas.toString() : result;
    }

    public Object executeCodeBlock(String block, String startCoord) {
        // set the coordinate to the start of the code block
        if (startCoord == null) coordRunTime.nouveauBloc(block);
        else coordRunTime.setCoord(startCoord);

        var scope = coordCompileDict.get(coordRunTime.getScope());

        var currentBlock = coordRunTime.copy();

        Object result = "[]";
        Statement statement = null;

        while (executionActive && canExecute && currentBlock.isSubCoordinate(coordRunTime)) {
            // System.out.println(coordRunTime);
            // get la ligne a executer dans le dictionnaire de coordonnees
            statement = scope.get(coordRunTime.toString());

            if (statement instanceof Statement.EndOfProgramStatement) {
                coordRunTime.setCoord(null);
                executionActive = false;
                break;
            }


            // s'il y a une erreur dans l'execution, on arr�te l'execution et on �crit le message d'erreur dans la console de l'app
            try {
                // execution de la ligne et enregistrement du resultat dans la variable du meme nom
                result = statement.execute();

                if (result instanceof Data) {
                    datas.add((Data) result);
                }

                if (datas.size() >= MAX_DATA_BEFORE_SEND) {
                    synchronized (datas) {
                        return datas.toString();
                    }
                }
            } catch (StopSendData e) {
                return e.getDataString();

            } catch (StopGetInfo e) {
                datas.add(e.getData());
                return datas.toString();

            } catch (StopSetInfo e) {
                datas.add(e.getData());

            } catch (ASCErrors.ASCError e) {
                // si l'erreur lancee est de type ASErreur.ErreurExecution (Voir ASErreur.java),
                // on l'affiche et on arrete l'execution du programme
                datas.add(e.getAsData(this));
                arreterExecution();
                e.afficher(this);
                result = null;
                break;

            } catch (RuntimeException e) {
                // s'il y a une erreur, mais que ce n'est pas une erreur se trouvant dans ASErreur, c'est une
                // erreur de syntaxe, comme l'autre type d'erreur, on l'affiche et on arrete l'execution du programme
                e.printStackTrace();
                datas.add(new ErreurSyntaxe("Une erreur interne inconnue est survenue lors de l'ex\u00E9cution de la ligne, v\u00E9rifiez que la syntaxe est valide")
                        .getAsData(this));
                if (debug) System.out.println(coordRunTime);
                arreterExecution();
                result = null;
                break;
            }
            // on passe a la coordonnee suivante
            coordRunTime.plusUn();
        }
        return (statement instanceof Statement.EndOfProgramStatement || !executionActive || result == null) ? datas.toString() : result;
    }


    /**
     * @param statement the statement to execute
     * @return Pair&lt;Boolean, String> : <ul>
     * <li>&lt;true, result> if the execution is not finished</li>
     * <li>&lt;false, result> if the execution is finished</li>
     * <li>&lt;null, result> if the execution is finished and the result should be returned as is</li>
     * </ul>
     */
    public Pair<Boolean, Object> executeStatement(Statement statement) {
        Object result = "[]";
        try {
            // execution de la ligne et enregistrement du resultat dans la variable du meme nom
            result = statement.execute();

            if (result instanceof Data) {
                datas.add((Data) result);

            } else if (result != null && !coordRunTime.getScope().equals("main")) {
                // ne sera vrai que si l'on retourne d'une fonction
                return new Pair<>(false, result);
            }

//                if (datas.size() >= MAX_DATA_BEFORE_SEND) {
//                    synchronized (datas) {
//                        return datas.toString();
//                    }
//                }
        } catch (StopSendData e) {
            return new Pair<>(null, e.getDataString());

        } catch (StopGetInfo e) {
            datas.add(e.getData());
            return new Pair<>(null, datas.toString());

        } catch (StopSetInfo e) {
            datas.add(e.getData());

        } catch (ASCErrors.ASCError e) {
            // si l'erreur lancee est de type ASErreur.ErreurExecution (Voir ASErreur.java),
            // on l'affiche et on arrete l'execution du programme
            datas.add(e.getAsData(this));
            arreterExecution();
            e.afficher(this);
            return new Pair<>(false, null);

        } catch (RuntimeException e) {
            // s'il y a une erreur, mais que ce n'est pas une erreur se trouvant dans ASErreur, c'est une
            // erreur de syntaxe, comme l'autre type d'erreur, on l'affiche et on arrete l'execution du programme
            e.printStackTrace();
            datas.add(new ErreurSyntaxe("Une erreur interne inconnue est survenue lors de l'ex\u00E9cution de la ligne, v\u00E9rifiez que la syntaxe est valide")
                    .getAsData(this));
            if (debug) System.out.println(coordRunTime);
            arreterExecution();
            return new Pair<>(false, null);
        }
        // on passe a la coordonnee suivante
        coordRunTime.plusUn();
        return new Pair<>(true, result);
    }

    /**
     * fonction executant le scope principal ("main")
     */
    public JSONArray executerMain(boolean resume) {
        executionActive = true;
        // sert au calcul du temps qu'a pris le code pour etre execute
        LocalDateTime before = LocalDateTime.now();

        if (obtenirCoordCompileDict().get("main").isEmpty()) {
            return new JSONArray();
        }

        String result;

        if (!resume) {
            // créer scopeInstance globale
            executorState
                    .getScopeManager()
                    .pushCurrentScopeInstance(executorState
                            .getScopeManager()
                            .getCurrentScope()
                            .makeScopeInstance(null));
            executerScope("main", null, null);
        } else resumeExecution();

        result = datas.toString();

        var returnData = new JSONArray(result);
        /*
         * affiche si l'execution s'est deroulee sans probleme ou si elle a ete interrompue par une erreur
         * affiche le temps qu'a pris l'execution du programme (au complet ou jusqu'a l'interruption)
         */
        if (coordRunTime.toString() == null || !executionActive) {
            if (debug)
                System.out.println("templates/blank/java/execution " + (executionActive ? "done" : "interruped") + " in " +
                                   (LocalDateTime.now().toLocalTime().toNanoOfDay() - before.toLocalTime().toNanoOfDay()) / Math.pow(10, 9) + " seconds\n");
            // boolean servant a indique que l'execution est terminee
            executionActive = false;
            reset();
            returnData.put(Data.endOfExecution());
        }
        datas.clear();

        return returnData;
    }

    public Object resumeExecution() {
        Coordinate coordActuel = obtenirCoordRunTime();
        return executerScope(coordActuel.getScope(), null, coordActuel.toString());
    }

    /**
     * reset tout a neuf pour la prochaine execution
     */
    private void reset() {
        executorState.getScopeManager().resetAllScope();
        executorState.getScopeManager().makeNewCurrentScope();
        //ASScope.resetAllScope();
        // créer le scope global
        //ASScope.makeNewCurrentScope();

        // supprime les variables, fonctions et iterateurs de la memoire
        datas.clear();

        // ASFonctionManager.reset();

        // moduleManager.utiliserModuleBuitlins();

        // remet la coordonnee d'execution au debut du programme
        coordRunTime.setCoord(debutCoord.toString());
    }
}










