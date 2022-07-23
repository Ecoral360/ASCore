package ascore;

import org.ascore.ASCore;
import org.junit.Test;

import java.io.IOException;

public class TestBasicTemplate {

    @Test
    public void testAfficher() {
        try {
            ASCore.makeProject("./src/test/test-basic-template", "testLang", ASCore.Template.BASIC, ASCore.Lang.JAVA, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
