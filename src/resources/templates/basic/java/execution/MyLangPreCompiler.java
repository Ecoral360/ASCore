package mylang.execution;

import org.ascore.executor.ASCPrecompiler;

/**
 * This class is used to precompile the source code. Edit the {@link #preCompile(String)} method to change the precompilation behavior.
 * <br>
 * PreCompilation is <b>guarantied</b> to be executed before the compilation of the source code.
 */
public class MyLangPreCompiler extends ASCPrecompiler {

    /**
     * This method is used to precompile the source code.
     *
     * @param program The source code to precompile as an array of strings.
     * @return The precompiled source code as an array of strings. (aka the actual code that will be compiled)
     */
    @Override
    public String preCompile(String program) {
        return program;
    }

}
