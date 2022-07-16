package mylang.execution;

import org.ascore.executor.ASCPrecompiler;

/**
 * This class is used to precompile the source code. Edit the {@link #preCompile(String)} method to change the precompilation behavior.
 * <br>
 * Precompilation is <b>garantied</b> to be executed before the compilation of the source code.
 */
public class MyLangPreCompiler extends ASCPrecompiler {

    /**
     * This method is used to precompile the source code.
     *
     * @param strings The source code to precompile as an array of strings.
     * @return The precompiled source code as an array of strings. (aka the actual code that will be compiled)
     */
    @Override
    public String preCompile(String strings) {
        return strings;
    }

}
