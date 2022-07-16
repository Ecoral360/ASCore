package utils;

import org.ascore.errors.ASCErrors;
import org.ascore.managers.data.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class ASCoreExecutionTester {
    private final JSONArray executionResult;
    private int currentIdx = 0;

    public ASCoreExecutionTester(JSONArray executionResult) {
        this.executionResult = executionResult;
    }

    //----------------- utils -----------------//

    private void assertHasNext() {
        assertTrue(
                "There are no more execution results to test",
                executionResult.length() > currentIdx
        );
    }

    private JSONObject getNextAction() {
        assertHasNext();
        return executionResult.getJSONObject(currentIdx);
    }

    public boolean isDone() {
        return executionResult.length() <= currentIdx;
    }

    //----------------- tests -----------------//

    public ASCoreExecutionTester prints(Object message) {
        message = message.toString();
        var action = getNextAction();
        assertEquals(300, action.getInt("id"));
        assertEquals(message, action.getJSONArray("p").getString(0));
        currentIdx++;
        return this;
    }

    public ASCoreExecutionTester throwsASErreur(Class<? extends ASCErrors.ASCError> erreurClass) {
        ASCErrors.ASCError erreur = null;
        try {
            erreur = erreurClass.getConstructor(String.class).newInstance("");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignore) {
        }
        assertNotNull(
                "There are not constructor for the error class '" + erreurClass.getSimpleName() + "'",
                erreur
        );
        var action = getNextAction();
        assertEquals(400, action.getInt("id"));
        assertEquals(erreur.getNomErreur(), action.getJSONArray("p").getString(0));
        currentIdx++;
        return this;
    }

    public ASCoreExecutionTester does(Data.Id id) {
        var action = getNextAction();
        assertEquals(id.getId(), action.getInt("id"));
        currentIdx++;
        return this;
    }

    public ASCoreExecutionTester does(Data.Id id, Object... params) {
        var action = getNextAction();
        assertEquals(id.getId(), action.getInt("id"));
        var actualParams = action.getJSONArray("p");
        assertEquals(params.length, actualParams.length());
        for (int i = 0; i < params.length; i++) {
            assertEquals(params[i], actualParams.get(i));
        }
        currentIdx++;
        return this;
    }

    public ASCoreExecutionTester ends() {
        var action = getNextAction();
        assertEquals(0, action.getInt("id"));
        currentIdx++;
        assertEquals(executionResult.length(), currentIdx);
        return this;
    }

    public ASCoreExecutionTester asksForDataResponse() {
        var action = getNextAction();
        var id = action.getInt("id");
        var dataId = Data.Id.dataIdFromId(id);
        assertNotNull("There are no Data.Id defined with id '" + id + "'", dataId);
        assertTrue(
                "Data.Id that ask for data must have a name that starts with 'GET_' (Data.Id name was '" + dataId.name() + "')",
                dataId.name().startsWith("GET")
        );
        currentIdx++;
        assertEquals(executionResult.length(), currentIdx);
        return this;
    }

    public ASCoreExecutionTester asksForDataResponse(Data.Id id) {
        this.does(id);
        assertEquals(executionResult.length(), currentIdx);
        return this;
    }

    public ASCoreExecutionTester asksForDataResponse(Data.Id id, Object... params) {
        this.does(id, params);
        assertEquals(executionResult.length(), currentIdx);
        return this;
    }


}
