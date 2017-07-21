package com.vuze.itunes.impl.osx.cocoa.applescript;

import com.vuze.itunes.impl.osx.cocoa.Script;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * implementation of the {@link com.vuze.itunes.impl.osx.cocoa.Script} interface that builds the source code of the
 * script using {@link java.text.MessageFormat#format(String, Object[])}.
 *
 * @author olemarchand
 */
public class ParameterizedScript implements Script {
    private final String sourceCodeTemplate;
    private final Object[] parameters;

    public ParameterizedScript( String sourceCodeTemplate, Object... parameters ) {
        assert sourceCodeTemplate != null;
        this.sourceCodeTemplate = sourceCodeTemplate;
        this.parameters = parameters;
    }

    public String getSourceCode() {
        return MessageFormat.format( sourceCodeTemplate, parameters );
    }


    public String toString() {
        return "ParameterizedScript{" +
               "parameters=" + ( parameters == null ? null : Arrays.asList( parameters ) ) +
               ", sourceCodeTemplate='" + sourceCodeTemplate + '\'' +
               '}';
    }
}
