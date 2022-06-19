package test.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static test.utils.AliveScriptTester.isDone;
import static test.utils.AliveScriptTester.resetExecuteur;

public class AbstractTestASCore {
    protected final boolean DEBUG;


    public AbstractTestASCore(boolean debug) {
        DEBUG = debug;
    }

    @BeforeEach
    public void setup() {
        resetExecuteur(DEBUG);
    }

    @AfterEach
    public void checkup() {
        assertTrue(isDone(), "The test fails to cover all the data returned by the Executeur");
    }

}
