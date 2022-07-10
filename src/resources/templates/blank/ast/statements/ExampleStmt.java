package mylang.ast.statements;

import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.ast.buildingBlocs.programmes.Declarer;
import org.ascore.executor.Coordinate;
import org.ascore.executor.ASCExecutor;
import org.ascore.tokens.Token;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Example of the implementation of a statement.
 *
 * @author Mathis Laroche
 */
public class ExampleStmt extends Statement {

    /**
     * If the statement doesn't need access to the executor {@link ASCExecutor} to work
     */
    public ExampleStmt() {
        super();
    }

    /**
     * If the statement needs access to the executor {@link ASCExecutor} to work
     *
     * @param executorInstance the executor instance
     */
    public ExampleStmt(@NotNull ASCExecutor executorInstance) {
        super(executorInstance);
    }

    /**
     * Method that describes the effect of the statement
     *
     * @return Can return several things, which causes several effects:
     * <ul>
     *     <li>
     *         <code>null</code> -> continues the execution to the next line
     *         (most of the statements should return <code>null</code>)
     *     </li>
     *     <li>
     *         <code>instance of Data</code> -> data to add to the list of data kept by the executor
     *     </li>
     *     <li>
     *         other -> if the statement is executed inside a function, this value is returned by the function. Else, the value is ignored
     *     </li>
     * </ul>
     */
    @Override
    public Object execute() {
        return null;
    }


    /**
     * <b>This method is called when the statement is compiled (at compile time).</b><br>
     * The override of this method is not necessary, but is useful when the goal is to change
     * the coordinate of the next line to compile by the {@link ASCExecutor executor} (for example, in a loop or an if statement)
     * <p>
     *
     * @param coord the current coordinate
     * @param ligne the current line, tokenized
     * @return the coordinate of the next line to compile
     */
    @Override
    public Coordinate getNextCoordinate(Coordinate coord, List<Token> ligne) {
        return super.getNextCoordinate(coord, ligne);
    }
}
