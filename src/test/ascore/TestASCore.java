package test.ascore;

import org.junit.jupiter.api.*;
import test.utils.AbstractTestASCore;

import static test.utils.AliveScriptTester.assertCompiles;
import static test.utils.AliveScriptTester.assertExecution;

public class TestASCore extends AbstractTestASCore {
    public TestASCore() {
        super(true);
    }

    @Test
    public void testAfficher() {
        assertCompiles("""
                afficher "bonjour"
                afficher 12
                afficher (-333)
                afficher (-23.11)
                afficher {1, 2, 3, 4}
                afficher vrai
                afficher ([5, 6, 7])
                """);

        assertExecution()
                .prints("bonjour")
                .prints("12")
                .prints("-333")
                .prints("-23.11")
                .prints("[1, 2, 3, 4]")
                .prints("vrai")
                .prints("[5, 6, 7]")
                .ends();
    }
}




























